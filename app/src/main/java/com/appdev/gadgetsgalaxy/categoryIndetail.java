package com.appdev.gadgetsgalaxy;

import static androidx.navigation.fragment.FragmentKt.findNavController;

import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.GridLayoutManager;

import com.appdev.gadgetsgalaxy.data.Category_info;
import com.appdev.gadgetsgalaxy.data.Product_info;
import com.appdev.gadgetsgalaxy.databinding.AddCategoryBinding;
import com.appdev.gadgetsgalaxy.databinding.FragmentCategoryIndetailBinding;
import com.appdev.gadgetsgalaxy.recyclerview.Product_image_adapter;
import com.appdev.gadgetsgalaxy.utils.FirebaseUtil;
import com.appdev.gadgetsgalaxy.utils.Utility;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class categoryIndetail extends Fragment {
    FragmentCategoryIndetailBinding categoryInDetailBinding;
    AddCategoryBinding addCategoryBinding;
    BottomSheetDialog bottomSheetDialog;
    private ActivityResultLauncher<String> getContentLauncher;
    Uri imageUri;
    Category_info categoryObject;
    DatabaseReference categoryRef;
    ValueEventListener OurCategoryListener;
    Dialog progressDialog;
    NavController navController;

    Product_image_adapter product_image_adapter;

    List<Product_info> productInfoList = new ArrayList<>();
    ValueEventListener productsListener;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        categoryInDetailBinding = FragmentCategoryIndetailBinding.inflate(inflater, container, false);
        navController = findNavController(this);
        categoryObject = categoryIndetailArgs.fromBundle(getArguments()).getCategoryInDetail();
        SetupViews(categoryObject);
        imageUri = Uri.parse(categoryObject.getImageUrl());
        progressDialog = new Dialog(requireContext());

        String userType = Utility.getUserTypeFromPrefs(categoryInDetailBinding.getRoot().getContext());
        if (!userType.trim().isEmpty()) {
            if ("ADMIN".equals(userType)) {
                categoryInDetailBinding.delLayout.setVisibility(View.VISIBLE);
            } else if ("USER".equals(userType)) {
                categoryInDetailBinding.delLayout.setVisibility(View.GONE);
            }
        }

        product_image_adapter = new Product_image_adapter(productInfoList, this::navigateWithInfo);
        categoryInDetailBinding.rv.setLayoutManager(new GridLayoutManager(getContext(), 2));
        categoryInDetailBinding.rv.setAdapter(product_image_adapter);

        getContentLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        imageUri = uri;
                        Glide.with(requireContext()).load(uri).into(addCategoryBinding.image);
                        ViewGroup.LayoutParams params = addCategoryBinding.image.getLayoutParams();
                        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
                        addCategoryBinding.image.setLayoutParams(params);
                    } else {
                        imageUri = null;
                        Toast.makeText(requireContext(), "No image selected", Toast.LENGTH_SHORT).show();
                    }
                });

        checkAuth();

        categoryInDetailBinding.floatingButton.setOnClickListener(view -> {
            bottomSheetDialog = new BottomSheetDialog(
                    requireContext());
            addCategoryBinding = AddCategoryBinding.inflate(getLayoutInflater());
            addCategoryBinding.closeBottomSheet.setOnClickListener(view1 -> bottomSheetDialog.dismiss());
            bottomSheetDialog.setContentView(addCategoryBinding.getRoot());

            ViewGroup.LayoutParams layoutParams = addCategoryBinding.image.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            addCategoryBinding.image.setLayoutParams(layoutParams);
            addCategoryBinding.image.setScaleType(ImageView.ScaleType.FIT_XY);

            Glide.with(requireContext()).load(categoryObject.getImageUrl()).placeholder(R.drawable.placeholder).into(addCategoryBinding.image);
            addCategoryBinding.descData.setText(categoryObject.getCatDescription());
            addCategoryBinding.titlefield.setText(categoryObject.getCatTitle());
            addCategoryBinding.savecatbtn.setText("Update category");


            addCategoryBinding.imageSelectionBtn.setOnClickListener(v -> {
                getContentLauncher.launch("image/*");
            });

            addCategoryBinding.savecatbtn.setOnClickListener(v -> {
                addCategoryBinding.pg.setVisibility(View.VISIBLE);
                addCategoryBinding.savecatbtn.setVisibility(View.INVISIBLE);
                String newDesc = addCategoryBinding.descData.getText().toString();
                String newTitle = addCategoryBinding.titlefield.getText().toString();
                if (newTitle.trim().isEmpty() || newDesc.trim().isEmpty() || imageUri == null) {
                    Toast.makeText(requireContext(), "Fill the missing fields !", Toast.LENGTH_SHORT).show();
                    addCategoryBinding.pg.setVisibility(View.INVISIBLE);
                    addCategoryBinding.savecatbtn.setVisibility(View.VISIBLE);
                } else if (newTitle.equals(categoryObject.getCatTitle()) && newDesc.equals(categoryObject.getCatDescription()) && imageUri.equals(Uri.parse(categoryObject.getImageUrl()))) {
                    Toast.makeText(requireContext(), "No changes have been made yet !", Toast.LENGTH_SHORT).show();
                    addCategoryBinding.pg.setVisibility(View.INVISIBLE);
                    addCategoryBinding.savecatbtn.setVisibility(View.VISIBLE);
                } else {
                    FirebaseUtil.getFirebaseDatabase().getReference().child("Categories").child(newTitle)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists() && !newTitle.equals(categoryObject.getCatTitle())) {
                                        Toast.makeText(requireContext(), "Category name already exists!", Toast.LENGTH_SHORT).show();
                                        addCategoryBinding.pg.setVisibility(View.INVISIBLE);
                                        addCategoryBinding.savecatbtn.setVisibility(View.VISIBLE);
                                    } else {
                                        if (imageUri.equals(Uri.parse(categoryObject.getImageUrl()))) {
                                            Category_info updatedData = new Category_info(newTitle, categoryObject.getImageUrl(), newDesc);
                                            updateData(updatedData);
                                        } else {
                                            StorageReference imageRef = FirebaseUtil.getStorageReference().child(System.currentTimeMillis() + ".jpg");
                                            imageRef.putFile(imageUri)
                                                    .addOnSuccessListener(taskSnapshot -> imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                                        String downloadUrl = uri.toString();
                                                        Category_info updatedCategoryInfo = new Category_info(newTitle, downloadUrl, newDesc);
                                                        updateData(updatedCategoryInfo);
                                                    }))
                                                    .addOnFailureListener(e -> {
                                                        Toast.makeText(requireContext(), "Failed to upload image to storage", Toast.LENGTH_SHORT).show();
                                                        addCategoryBinding.pg.setVisibility(View.INVISIBLE);
                                                        addCategoryBinding.savecatbtn.setVisibility(View.VISIBLE);
                                                    });
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(requireContext(), "Error checking category existence", Toast.LENGTH_SHORT).show();
                                    addCategoryBinding.pg.setVisibility(View.INVISIBLE);
                                    addCategoryBinding.savecatbtn.setVisibility(View.VISIBLE);
                                }
                            });
                }
            });


            Objects.requireNonNull(bottomSheetDialog.getWindow()).setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            bottomSheetDialog.setCancelable(false);
            bottomSheetDialog.show();
        });

        return categoryInDetailBinding.getRoot();
    }

    private void navigateWithInfo(Product_info productInfo) {
    }

    private void checkAuth() {
        String userType = Utility.getUserTypeFromPrefs(categoryInDetailBinding.getRoot().getContext());
        if (!userType.trim().isEmpty()) {
            if ("ADMIN".equals(userType)) {
                categoryInDetailBinding.floatingButton.setVisibility(View.VISIBLE);
            } else if ("USER".equals(userType)) {
                categoryInDetailBinding.floatingButton.setVisibility(View.GONE);
            }
        }
    }

    private void updateData(Category_info updatedCategoryInfo) {
        DatabaseReference databaseReference = FirebaseUtil.getFirebaseDatabase().getReference().child("Categories");
        if (updatedCategoryInfo.getCatTitle().equals(categoryObject.getCatTitle())) {
            databaseReference.child(updatedCategoryInfo.getCatTitle()).setValue(updatedCategoryInfo)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(requireContext(), "Category updated successfully", Toast.LENGTH_SHORT).show();
                        addCategoryBinding.pg.setVisibility(View.INVISIBLE);
                        bottomSheetDialog.dismiss();
                        categoryObject = updatedCategoryInfo;
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(requireContext(), "Failed to update Category", Toast.LENGTH_SHORT).show();
                        addCategoryBinding.pg.setVisibility(View.INVISIBLE);
                        addCategoryBinding.savecatbtn.setVisibility(View.VISIBLE);
                    });
        } else {
            databaseReference.child(categoryObject.getCatTitle()).removeValue()
                    .addOnSuccessListener(aVoid -> databaseReference.child(updatedCategoryInfo.getCatTitle()).setValue(updatedCategoryInfo)
                            .addOnSuccessListener(aVoid1 -> {
                                setupCategoryListener(updatedCategoryInfo.getCatTitle());
                                Toast.makeText(requireContext(), "Category updated successfully", Toast.LENGTH_SHORT).show();
                                addCategoryBinding.pg.setVisibility(View.INVISIBLE);
                                bottomSheetDialog.dismiss();
                                categoryObject = updatedCategoryInfo;
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(requireContext(), "Failed to update Category", Toast.LENGTH_SHORT).show();
                                addCategoryBinding.pg.setVisibility(View.INVISIBLE);
                                addCategoryBinding.savecatbtn.setVisibility(View.VISIBLE);
                            }))
                    .addOnFailureListener(e -> {
                        Toast.makeText(requireContext(), "Failed to update Category", Toast.LENGTH_SHORT).show();
                        addCategoryBinding.pg.setVisibility(View.INVISIBLE);
                        addCategoryBinding.savecatbtn.setVisibility(View.VISIBLE);
                    });
        }
    }

    public void showProgressDialog() {
        progressDialog.setContentView(R.layout.progress_bar);
        Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void SetupViews(Category_info categoryObject) {
        Glide.with(requireContext()).load(categoryObject.getImageUrl()).placeholder(R.drawable.placeholder).into(categoryInDetailBinding.imageIndetail);
        categoryInDetailBinding.title.setText(categoryObject.getCatTitle());
        categoryInDetailBinding.desc.setText(categoryObject.getCatDescription());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        categoryInDetailBinding.delBtn.setOnClickListener(v -> {
            if (!productInfoList.isEmpty()) {
                Toast.makeText(requireContext(), "You need to remove all products under this category first !", Toast.LENGTH_LONG).show();
            } else {
                showProgressDialog();
                FirebaseUtil.getFirebaseDatabase().getReference().child("Categories").child(categoryObject.getCatTitle().trim()).removeValue().addOnSuccessListener(taskDone -> {
                    dismissProgressDialog();
                    Toast.makeText(requireContext(), "Category Deleted Successfully !", Toast.LENGTH_LONG).show();
                    navController.popBackStack();
                }).addOnFailureListener(failed -> {
                    dismissProgressDialog();
                    Toast.makeText(requireContext(), failed.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                });

            }
        });

        productsListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productInfoList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Product_info product = snapshot.getValue(Product_info.class);
                    if (product != null) {
                        productInfoList.add(product);
                    }
                }
                changeVisibility();
                product_image_adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };

        FirebaseUtil.getFirebaseDatabase().getReference().child("Products")
                .orderByChild("category")
                .equalTo(categoryObject.getCatTitle())
                .addValueEventListener(productsListener);


        OurCategoryListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Category_info cat = snapshot.getValue(Category_info.class);
                if (cat != null) {
                    SetupViews(cat);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };
        setupCategoryListener(categoryObject.getCatTitle());

        categoryInDetailBinding.backBtn.setOnClickListener(view1 -> {
            findNavController(this).popBackStack();
        });
    }

    private void changeVisibility() {
        categoryInDetailBinding.pg.setVisibility(View.GONE);
        if (productInfoList.isEmpty()) {
            categoryInDetailBinding.empty.setVisibility(View.VISIBLE);
            categoryInDetailBinding.rv.setVisibility(View.GONE);
        } else {
            categoryInDetailBinding.rv.setVisibility(View.VISIBLE);
            categoryInDetailBinding.empty.setVisibility(View.GONE);
        }
    }

    private void setupCategoryListener(String categoryTitle) {
        if (categoryRef != null && OurCategoryListener != null) {
            categoryRef.removeEventListener(OurCategoryListener); // Remove the old listener
        }

        categoryRef = FirebaseUtil.getFirebaseDatabase().getReference()
                .child("Categories").child(categoryTitle);

        categoryRef.addValueEventListener(OurCategoryListener);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // Remove the ValueEventListener to prevent memory leaks
        if (OurCategoryListener != null) {
            FirebaseUtil.getFirebaseDatabase().getReference()
                    .child("Categories").child(categoryObject.getCatTitle())
                    .removeEventListener(OurCategoryListener);
        }
    }
}
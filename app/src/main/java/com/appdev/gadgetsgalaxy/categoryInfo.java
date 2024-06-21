package com.appdev.gadgetsgalaxy;

import static androidx.navigation.fragment.FragmentKt.findNavController;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.recyclerview.widget.GridLayoutManager;

import com.appdev.gadgetsgalaxy.data.Category_info;
import com.appdev.gadgetsgalaxy.databinding.AddCategoryBinding;
import com.appdev.gadgetsgalaxy.databinding.FragmentCategoryInfoBinding;
import com.appdev.gadgetsgalaxy.recyclerview.Category_Image_Adapter;
import com.appdev.gadgetsgalaxy.utils.FirebaseUtil;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class categoryInfo extends Fragment {
    private FragmentCategoryInfoBinding categoryInfoBinding;
    Category_Image_Adapter categoryImageAdapter;

    List<Category_info> categoryInfoList = new ArrayList<>();
    AddCategoryBinding addCategoryBinding;
    BottomSheetDialog bottomSheetDialog;
    private ActivityResultLauncher<String> getContentLauncher;
    Uri imageUri;
    ValueEventListener categoryListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        categoryInfoBinding = FragmentCategoryInfoBinding.inflate(inflater, container, false);


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
                        Toast.makeText(requireContext(), "No image selected", Toast.LENGTH_SHORT).show();
                    }
                });

        categoryImageAdapter = new Category_Image_Adapter(categoryInfoList, this::navigateWithInfo);
        categoryInfoBinding.rv.setLayoutManager(new GridLayoutManager(getContext(), 2));
        categoryInfoBinding.rv.setAdapter(categoryImageAdapter);
        categoryInfoBinding.backBtn.setOnClickListener(view -> {
            findNavController(this).popBackStack();
        });
        categoryInfoBinding.floatingButton.setOnClickListener(view -> {
            bottomSheetDialog = new BottomSheetDialog(
                    requireContext());
            addCategoryBinding = AddCategoryBinding.inflate(getLayoutInflater());
            addCategoryBinding.closeBottomSheet.setOnClickListener(view1 -> bottomSheetDialog.dismiss());
            addCategoryBinding.imageSelectionBtn.setOnClickListener(v -> {
                getContentLauncher.launch("image/*");
            });

            addCategoryBinding.savecatbtn.setOnClickListener(v -> {
                addCategoryBinding.pg.setVisibility(View.VISIBLE);
                addCategoryBinding.savecatbtn.setVisibility(View.INVISIBLE);
                if (imageUri != null) {
                    if (addCategoryBinding.titlefield.getText().toString().trim().isEmpty() || addCategoryBinding.descData.getText().toString().trim().isEmpty()) {
                        Toast.makeText(requireContext(), "Fill the missing fields !", Toast.LENGTH_SHORT).show();
                        addCategoryBinding.pg.setVisibility(View.INVISIBLE);
                        addCategoryBinding.savecatbtn.setVisibility(View.VISIBLE);
                    } else {
                        String categoryName = addCategoryBinding.titlefield.getText().toString();
                        String categoryDescription = addCategoryBinding.descData.getText().toString();

                        FirebaseUtil.getFirebaseDatabase().getReference().child("Categories").child(categoryName)
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            // Category already exists
                                            Toast.makeText(requireContext(), "Category already exists!", Toast.LENGTH_SHORT).show();
                                            addCategoryBinding.pg.setVisibility(View.INVISIBLE);
                                            addCategoryBinding.savecatbtn.setVisibility(View.VISIBLE);
                                        } else {
                                            // Category does not exist, proceed to create it
                                            StorageReference imageRef = FirebaseUtil.getStorageReference().child(System.currentTimeMillis() + ".jpg");
                                            imageRef.putFile(imageUri)
                                                    .addOnSuccessListener(taskSnapshot -> imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                                        String downloadUrl = uri.toString();
                                                        Category_info categoryInfo = new Category_info(categoryName, downloadUrl, categoryDescription);
                                                        FirebaseUtil.getFirebaseDatabase().getReference().child("Categories").child(categoryName).setValue(categoryInfo)
                                                                .addOnSuccessListener(aVoid -> {
                                                                    Toast.makeText(requireContext(), "Category created successfully", Toast.LENGTH_SHORT).show();
                                                                    addCategoryBinding.pg.setVisibility(View.INVISIBLE);
                                                                    bottomSheetDialog.dismiss();
                                                                })
                                                                .addOnFailureListener(e -> {
                                                                    Toast.makeText(requireContext(), "Failed to create Category", Toast.LENGTH_SHORT).show();
                                                                    addCategoryBinding.pg.setVisibility(View.INVISIBLE);
                                                                    addCategoryBinding.savecatbtn.setVisibility(View.VISIBLE);
                                                                });
                                                    }))
                                                    .addOnFailureListener(e -> {
                                                        Toast.makeText(requireContext(), "Failed to upload image to storage", Toast.LENGTH_SHORT).show();
                                                        addCategoryBinding.pg.setVisibility(View.INVISIBLE);
                                                        addCategoryBinding.savecatbtn.setVisibility(View.VISIBLE);
                                                    });
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
                } else {
                    Toast.makeText(requireContext(), "No image selected", Toast.LENGTH_SHORT).show();
                    addCategoryBinding.pg.setVisibility(View.INVISIBLE);
                    addCategoryBinding.savecatbtn.setVisibility(View.VISIBLE);

                }
            });

            bottomSheetDialog.setContentView(addCategoryBinding.getRoot());
            Objects.requireNonNull(bottomSheetDialog.getWindow()).setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            bottomSheetDialog.setCancelable(false);
            bottomSheetDialog.show();
        });

        return categoryInfoBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Add a ValueEventListener to listen for changes in the "Categories" node
        ValueEventListener categoryListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categoryInfoList.clear(); // Clear the list to avoid duplicates
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Category_info categoryInfo = dataSnapshot.getValue(Category_info.class);
                    categoryInfoList.add(categoryInfo);
                }
                changeVisibility();
                categoryImageAdapter.notifyDataSetChanged(); // Notify the adapter that the data has changed
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };

        // Attach the ValueEventListener to the "Categories" node
        FirebaseUtil.getFirebaseDatabase().getReference()
                .child("Categories")
                .addValueEventListener(categoryListener);

        // Store the listener in a member variable to remove it later
        this.categoryListener = categoryListener;
    }

    private void changeVisibility() {
            categoryInfoBinding.pg.setVisibility(View.GONE);
            if(categoryInfoList.isEmpty()){
                categoryInfoBinding.rv.setVisibility(View.GONE);
                categoryInfoBinding.catEmpty.setVisibility(View.VISIBLE);
            } else{
                categoryInfoBinding.catEmpty.setVisibility(View.GONE);
                categoryInfoBinding.rv.setVisibility(View.VISIBLE);
            }
    }

    private void navigateWithInfo(Category_info categoryInfo) {
        NavDirections action = categoryInfoDirections.actionCategoryInfoToCategoryIndetail(categoryInfo);
        findNavController(this).navigate(action);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // Remove the ValueEventListener to prevent memory leaks
        if (categoryListener != null) {
            FirebaseUtil.getFirebaseDatabase().getReference()
                    .child("Categories")
                    .removeEventListener(categoryListener);
        }
    }

}
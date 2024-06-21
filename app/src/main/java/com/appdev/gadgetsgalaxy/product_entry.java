package com.appdev.gadgetsgalaxy;

import static androidx.navigation.fragment.FragmentKt.findNavController;

import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.appdev.gadgetsgalaxy.data.Category_info;
import com.appdev.gadgetsgalaxy.data.Product_info;
import com.appdev.gadgetsgalaxy.databinding.FragmentProductEntryBinding;
import com.appdev.gadgetsgalaxy.databinding.ProgressBarBinding;
import com.appdev.gadgetsgalaxy.utils.FirebaseUtil;
import com.appdev.gadgetsgalaxy.utils.Utility;
import com.appdev.gadgetsgalaxy.utils.Validation;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class product_entry extends Fragment {

    FragmentProductEntryBinding productEntryBinding;
    private ActivityResultLauncher<String> getContentLauncher;

    ProgressBarBinding progressBarBinding;
    Dialog progressDialog;
    List<String> categoryList = new ArrayList<>();
    ValueEventListener categoryItemListener;


    Uri imageUri;
    Product_info productInfo = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        productEntryBinding = FragmentProductEntryBinding.inflate(inflater, container, false);


        progressBarBinding = ProgressBarBinding.inflate(inflater);
        progressDialog = new Dialog(requireContext());

        productInfo = product_entryArgs.fromBundle(getArguments()).getProduct();
        if (productInfo != null) {
            SetupViews(productInfo);
            imageUri = Uri.parse(productInfo.getImageUrl());
            productEntryBinding.titleTextView.setText("Update Product");
            productEntryBinding.submitBtn.setText("Update");
        }




        getContentLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        imageUri = uri;
                        Glide.with(requireContext()).load(uri).into(productEntryBinding.image);
                        ViewGroup.LayoutParams params = productEntryBinding.image.getLayoutParams();
                        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
                        productEntryBinding.image.setLayoutParams(params);
                    } else {
                        imageUri = null;
                        Toast.makeText(requireContext(), "No image selected", Toast.LENGTH_SHORT).show();
                    }
                });

        return productEntryBinding.getRoot();
    }

    private void SetupViews(Product_info productInfo) {
        String discounted = "";
        if (productInfo.getItem_discounted_price() > 0) {
            discounted = String.valueOf(productInfo.getItem_discounted_price());
        }

        ViewGroup.LayoutParams params = productEntryBinding.image.getLayoutParams();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        productEntryBinding.image.setLayoutParams(params);
        Glide.with(requireContext()).load(productInfo.getImageUrl()).into(productEntryBinding.image);
        productEntryBinding.descData.setText(productInfo.getDesc());
        productEntryBinding.titlefield.setText(productInfo.getItem_name());
        productEntryBinding.catSpin.setText(productInfo.getCategory());
        productEntryBinding.quantityfield.setText(String.valueOf(productInfo.getQuantity_available()));
        productEntryBinding.pricefield.setText(String.valueOf(productInfo.getItem_price()));
        productEntryBinding.modelSpin.setText(String.valueOf(productInfo.getModel()));
        if (!discounted.trim().isEmpty()) {
            productEntryBinding.onSale.setChecked(true);
            productEntryBinding.sales.setVisibility(View.VISIBLE);
            double percentage = ((productInfo.getItem_price() - productInfo.getItem_discounted_price()) / (double) productInfo.getItem_price()) * 100;
            int percentageInt = (int) percentage;
            String per = percentageInt + "%";
            productEntryBinding.sales.setText(per);
        }
    }

    private void updateProduct() {
        showProgressDialog();
        String valid = ValidationProcess();
        if (!valid.isEmpty()) {
            dismissProgressDialog();
            Toast.makeText(requireContext(), valid, Toast.LENGTH_SHORT).show();
        } else {
            DatabaseReference databaseReference = FirebaseUtil.getFirebaseDatabase().getReference().child("Products");

            databaseReference.orderByChild("item_name").equalTo(productInfo.getItem_name()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    int discountedPrice;
                    if (productEntryBinding.sales.getText().toString().isEmpty() || !productEntryBinding.onSale.isChecked()) {
                        discountedPrice = 0;
                    } else {
                        int percentage = Integer.parseInt(productEntryBinding.sales.getText().toString().replace("%", ""));
                        double discountMultiplier = 1 - (percentage / 100.0);
                        discountedPrice = (int) (Integer.parseInt(productEntryBinding.pricefield.getText().toString()) * discountMultiplier);
                    }

                    if (imageUri.toString().equals(productInfo.getImageUrl())) {
                        Product_info product = new Product_info(
                                productEntryBinding.titlefield.getText().toString(),
                                imageUri.toString(),
                                Integer.parseInt(productEntryBinding.pricefield.getText().toString()),
                                Integer.parseInt(productEntryBinding.modelSpin.getText().toString()),
                                discountedPrice, productInfo.getItem_id(),
                                Integer.parseInt(productEntryBinding.quantityfield.getText().toString()),
                                "0.0", productEntryBinding.descData.getText().toString(), productEntryBinding.catSpin.getText().toString(), productInfo.getProductCreated()
                        );
                        startUpdating(product);
                    } else {

                        StorageReference imageRef = FirebaseUtil.getStorageReference().child(System.currentTimeMillis() + ".jpg");
                        imageRef.putFile(imageUri).addOnSuccessListener(taskSnapshot ->
                                imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                    String downloadUrl = uri.toString();
                                    Product_info product = new Product_info(
                                            productEntryBinding.titlefield.getText().toString(),
                                            downloadUrl,
                                            Integer.parseInt(productEntryBinding.pricefield.getText().toString()),
                                            Integer.parseInt(productEntryBinding.modelSpin.getText().toString()),
                                            discountedPrice, productInfo.getItem_id(),
                                            Integer.parseInt(productEntryBinding.quantityfield.getText().toString()),
                                            "0.0", productEntryBinding.descData.getText().toString(), productEntryBinding.catSpin.getText().toString(), productInfo.getProductCreated()
                                    );
                                    startUpdating(product);
                                })
                        ).addOnFailureListener(e -> {
                            dismissProgressDialog();
                            Toast.makeText(requireContext(), "Failed to upload image to storage", Toast.LENGTH_SHORT).show();
                        });

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    dismissProgressDialog();
                }
            });
        }
    }

    public void startUpdating(Product_info productup) {
        FirebaseUtil.getFirebaseDatabase().getReference().child("Products").child(productup.getItem_id()).setValue(productup)
                .addOnSuccessListener(task -> {
                    dismissProgressDialog();
                    Toast.makeText(requireContext(), "Product updated successfully", Toast.LENGTH_SHORT).show();
                    findNavController(this).popBackStack();
                }).addOnFailureListener(e -> {
                    dismissProgressDialog();
                    Toast.makeText(requireContext(), "Failed to update Product", Toast.LENGTH_SHORT).show();
                });
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        categoryItemListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categoryList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Category_info categoryInfo = dataSnapshot.getValue(Category_info.class);
                    categoryList.add(categoryInfo.getCatTitle());
                }
                productEntryBinding.catSpin.setItems(categoryList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };

        FirebaseUtil.getFirebaseDatabase().getReference()
                .child("Categories")
                .addValueEventListener(categoryItemListener);



        productEntryBinding.imageSelectionBtn.setOnClickListener(v -> {
            getContentLauncher.launch("image/*");
        });

        productEntryBinding.submitBtn.setOnClickListener(v -> {
            if (productInfo == null && productEntryBinding.submitBtn.getText().toString().equals("Save")) {
                showProgressDialog();
                String valid = ValidationProcess();
                if (!valid.isEmpty()) {
                    dismissProgressDialog();
                    Toast.makeText(requireContext(), valid, Toast.LENGTH_SHORT).show();
                } else {
                    String newProductName = productEntryBinding.titlefield.getText().toString();
                    DatabaseReference databaseReference = FirebaseUtil.getFirebaseDatabase().getReference().child("Products");

                    // Query the database to check if a product with the same name already exists
                    databaseReference.orderByChild("item_name").equalTo(newProductName).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                dismissProgressDialog();
                                Toast.makeText(getContext(), "Product with the same name already exists.", Toast.LENGTH_SHORT).show();
                            } else {
                                // Proceed with adding the new product
                                addNewProduct(databaseReference);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            dismissProgressDialog();
                            Toast.makeText(getContext(), "Failed to check product name.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } else {
                updateProduct();
            }
        });
        productEntryBinding.backBtn.setOnClickListener(v -> {
            findNavController(this).popBackStack();
        });
        productEntryBinding.onSale.setOnClickListener(view1 -> {
            if (productEntryBinding.onSale.isChecked()) {
                productEntryBinding.sales.setVisibility(View.VISIBLE);
            } else {
                productEntryBinding.sales.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void addNewProduct(DatabaseReference databaseReference) {
        String productId = databaseReference.push().getKey();
        StorageReference imageRef = FirebaseUtil.getStorageReference().child(System.currentTimeMillis() + ".jpg");
        imageRef.putFile(imageUri).addOnSuccessListener(taskSnapshot ->
                imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    String downloadUrl = uri.toString();
                    int discountedPrice;
                    if (productEntryBinding.sales.getText().toString().isEmpty() || !productEntryBinding.onSale.isChecked()) {
                        discountedPrice = 0;
                    } else {
                        int percentage = Integer.parseInt(productEntryBinding.sales.getText().toString().replace("%", ""));
                        double discountMultiplier = 1 - (percentage / 100.0);
                        discountedPrice = (int) (Integer.parseInt(productEntryBinding.pricefield.getText().toString()) * discountMultiplier);
                    }
                    String date = new Utility().formatDate(System.currentTimeMillis());
                    Product_info product = new Product_info(
                            productEntryBinding.titlefield.getText().toString(),
                            downloadUrl,
                            Integer.parseInt(productEntryBinding.pricefield.getText().toString()),
                            Integer.parseInt(productEntryBinding.modelSpin.getText().toString()),
                            discountedPrice, productId,
                            Integer.parseInt(productEntryBinding.quantityfield.getText().toString()),
                            "0.0", productEntryBinding.descData.getText().toString(), productEntryBinding.catSpin.getText().toString(), date
                    );
                    databaseReference.child(productId).setValue(product).addOnCompleteListener(task -> {
                        dismissProgressDialog();
                        if (task.isSuccessful()) {
                            findNavController(this).popBackStack();
                            Toast.makeText(getContext(), "Product saved successfully.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Failed to save product.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }).addOnFailureListener(e -> {
                            dismissProgressDialog();
                            Toast.makeText(getContext(), "Failed to get download URL.", Toast.LENGTH_SHORT).show();
                        }
                )
        ).addOnFailureListener(e -> {
            dismissProgressDialog();
            Toast.makeText(getContext(), "Failed to upload image.", Toast.LENGTH_SHORT).show();
        });

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


    private String ValidationProcess() {
        String title = productEntryBinding.titlefield.getText().toString().trim();
        String price = productEntryBinding.pricefield.getText().toString().trim();
        String category = productEntryBinding.catSpin.getText().toString().trim();
        String model = productEntryBinding.modelSpin.getText().toString().trim();
        String quantity = productEntryBinding.quantityfield.getText().toString().trim();
        String desc = productEntryBinding.descData.getText().toString().trim();
        return new Validation().validateInputs(title, price, category, model, quantity, desc, imageUri);
    }
}
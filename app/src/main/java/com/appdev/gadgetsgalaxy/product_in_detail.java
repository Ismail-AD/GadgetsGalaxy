package com.appdev.gadgetsgalaxy;

import static androidx.navigation.fragment.FragmentKt.findNavController;

import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;

import com.appdev.gadgetsgalaxy.data.Product_info;
import com.appdev.gadgetsgalaxy.databinding.FragmentProductInDetailBinding;
import com.appdev.gadgetsgalaxy.utils.FirebaseUtil;
import com.appdev.gadgetsgalaxy.utils.Utility;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;


public class product_in_detail extends Fragment {
    FragmentProductInDetailBinding binding;
    Product_info productInfo;
    Uri imageUri;
    ValueEventListener ourListener;
    Dialog progressDialog;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Utility.isDarkModeActivated(requireActivity())) {
            Utility.status_bar_dark(requireActivity(), R.color.black);
        } else {
            Utility.status_bar(requireActivity(), R.color.white);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProductInDetailBinding.inflate(inflater, container, false);
        progressDialog = new Dialog(requireContext());


        productInfo = product_in_detailArgs.fromBundle(getArguments()).getProduct();
        checkViewAuthority();
        if (productInfo != null) {
            SetupViews(productInfo);
            imageUri = Uri.parse(productInfo.getImageUrl());
        }

        binding.floatingButton.setOnClickListener(v -> {
            NavDirections action = product_in_detailDirections.actionProductInDetailToProductEntry(productInfo);
            findNavController(this).navigate(action);
        });
        binding.backBtn.setOnClickListener(view1 -> {
            findNavController(this).popBackStack();
        });

        return binding.getRoot();
    }


    private void checkViewAuthority() {
        String userType = Utility.getUserTypeFromPrefs(binding.getRoot().getContext());
        if (!userType.trim().isEmpty()) {
            if ("ADMIN".equals(userType)) {
                binding.delBtn.setImageResource(R.drawable.baseline_delete_24);
                binding.addtoCart.setVisibility(View.GONE);
                binding.delBtn.setOnClickListener(v -> {
                    showProgressDialog();
                    FirebaseUtil.getFirebaseDatabase().getReference().child("Products").child(productInfo.getItem_id()).removeValue().addOnSuccessListener(taskD -> {
                        dismissProgressDialog();
                        Toast.makeText(getContext(), "Product Removed Successfully!", Toast.LENGTH_SHORT).show();
                        findNavController(this).popBackStack();
                    }).addOnFailureListener(failed -> {
                        dismissProgressDialog();
                        Toast.makeText(getContext(), failed.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    });

                });
            } else if ("USER".equals(userType)) {
                binding.delBtn.setImageResource(R.drawable.favorite_border_24);
                binding.floatingButton.setVisibility(View.GONE);
                binding.quantityLayout.setVisibility(View.GONE);
                binding.dateLayout.setVisibility(View.GONE);
                showProgressDialog();
                DatabaseReference wishlistRef = FirebaseUtil.getFirebaseDatabase()
                        .getReference()
                        .child("Wishlist")
                        .child(FirebaseUtil.getFirebaseAuth().getUid()).child(productInfo.getItem_id());
                wishlistRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            binding.delBtn.setImageResource(R.drawable.favorite_24);
                        }
                        dismissProgressDialog();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        dismissProgressDialog();
                    }
                });

                binding.addtoCart.setOnClickListener(v1 -> {
                    showProgressDialog();

                    DatabaseReference cartRef = FirebaseUtil.getFirebaseDatabase()
                            .getReference()
                            .child("Cart")
                            .child(FirebaseUtil.getFirebaseAuth().getUid());
                    String productId = productInfo.getItem_id();

                    cartRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            boolean productExists = false;
                            String existingProductPushKey = null;
                            Product_info existingProductInfo = null;

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Product_info cartProduct = snapshot.getValue(Product_info.class);
                                if (cartProduct != null && cartProduct.getItem_id().equals(productId)) {
                                    productExists = true;
                                    existingProductPushKey = snapshot.getKey();
                                    existingProductInfo = cartProduct;
                                    break;
                                }
                            }

                            if (productExists && existingProductInfo != null && existingProductPushKey != null) {
                                // Product already exists, update the quantity
                                int newQuantity = existingProductInfo.getSelectedQuantity() + 1;
                                cartRef.child(existingProductPushKey).child("selectedQuantity").setValue(newQuantity)
                                        .addOnSuccessListener(unused -> {
                                            dismissProgressDialog();
                                            Toast.makeText(getContext(), "Added to Cart Successfully", Toast.LENGTH_SHORT).show();
                                        })
                                        .addOnFailureListener(e -> {
                                            dismissProgressDialog();
                                            Toast.makeText(getContext(), "Failed to update the cart!", Toast.LENGTH_SHORT).show();
                                        });
                            } else {
                                // Product does not exist, add as a new item
                                productInfo.setSelectedQuantity(1);
                                String key = cartRef.push().getKey();
                                cartRef.child(key).setValue(productInfo)
                                        .addOnSuccessListener(unused -> {
                                            dismissProgressDialog();
                                            Toast.makeText(getContext(), "Added to Cart Successfully", Toast.LENGTH_SHORT).show();
                                        })
                                        .addOnFailureListener(e -> {
                                            dismissProgressDialog();
                                            Toast.makeText(getContext(), "Failed to add to the cart!", Toast.LENGTH_SHORT).show();
                                        });
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            dismissProgressDialog();
                            Toast.makeText(getContext(), "Failed to read cart data!", Toast.LENGTH_SHORT).show();
                        }
                    });


                });
                binding.delBtn.setOnClickListener(v -> {
                    showProgressDialog();
                    wishlistRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                wishlistRef.removeValue()
                                        .addOnSuccessListener(unused -> {
                                            dismissProgressDialog();
                                            binding.delBtn.setImageResource(R.drawable.favorite_border_24);
                                            Toast.makeText(getContext(), "Removed from Wishlist Successfully", Toast.LENGTH_SHORT).show();
                                        })
                                        .addOnFailureListener(e -> {
                                            dismissProgressDialog();
                                            Toast.makeText(getContext(), "Failed to remove from Wishlist", Toast.LENGTH_SHORT).show();
                                        });
                            } else {
                                // Product does not exist in the wishlist, add it
                                wishlistRef.setValue(productInfo)
                                        .addOnSuccessListener(unused -> {
                                            dismissProgressDialog();
                                            binding.delBtn.setImageResource(R.drawable.favorite_24);
                                            Toast.makeText(getContext(), "Added to Wishlist Successfully", Toast.LENGTH_SHORT).show();
                                        })
                                        .addOnFailureListener(e -> {
                                            dismissProgressDialog();
                                            Toast.makeText(getContext(), "Failed to add to Wishlist", Toast.LENGTH_SHORT).show();
                                        });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            dismissProgressDialog();
                        }
                    });

                });
            }
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


    private void SetupViews(Product_info productInfo) {
        String discounted = "";
        if (productInfo.getItem_discounted_price() > 0) {
            binding.slashSale.setVisibility(View.VISIBLE);
            binding.disprice.setVisibility(View.VISIBLE);
            binding.itemPrice.setTypeface(ResourcesCompat.getFont(binding.getRoot().getContext(), R.font.helvetica));
            discounted = String.valueOf(productInfo.getItem_discounted_price());
        } else {
            binding.slashSale.setVisibility(View.GONE);
            binding.disprice.setVisibility(View.GONE);
            binding.itemPrice.setTypeface(ResourcesCompat.getFont(binding.getRoot().getContext(), R.font.helveticaneuemedium));
        }


        String price = String.valueOf(productInfo.getItem_price());
        Glide.with(requireContext()).load(productInfo.getImageUrl()).placeholder(R.drawable.placeholder).into(binding.imageIndetail);
        binding.desc.setText(productInfo.getDesc());
        binding.pname.setText(productInfo.getItem_name() + "  " + productInfo.getModel());
        binding.cname.setText(productInfo.getCategory());
        binding.quantity.setText(String.valueOf(productInfo.getQuantity_available()));
        binding.itemPrice.setText("Rs: " + price);
        binding.date.setText(productInfo.getProductCreated());
        if (!discounted.trim().isEmpty()) {
            binding.disprice.setText("Rs: " + discounted);
        }

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Retrieve the product information from the dataSnapshot
                    productInfo = snapshot.getValue(Product_info.class);
                    SetupViews(productInfo);
                    imageUri = Uri.parse(productInfo.getImageUrl());

                } else {
                    // Handle the case where the product with the given ID does not exist
                    Toast.makeText(getContext(), "Product not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle potential errors
                Toast.makeText(getContext(), "Failed to retrieve product: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };

        FirebaseUtil.getFirebaseDatabase().getReference().child("Products").child(productInfo.getItem_id()).addValueEventListener(valueEventListener);
        this.ourListener = valueEventListener;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // Remove the ValueEventListener to prevent memory leaks
        if (ourListener != null) {
            FirebaseUtil.getFirebaseDatabase().getReference().child("Products").child(productInfo.getItem_id())
                    .removeEventListener(ourListener);
        }
    }
}
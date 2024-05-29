package com.appdev.gadgetsgalaxy;

import static androidx.navigation.fragment.FragmentKt.findNavController;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.appdev.gadgetsgalaxy.data.Product_info;
import com.appdev.gadgetsgalaxy.databinding.FragmentCartScreenBinding;
import com.appdev.gadgetsgalaxy.recyclerview.CartProducts_Adapter;
import com.appdev.gadgetsgalaxy.utils.FirebaseUtil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CartScreen extends Fragment {

    FragmentCartScreenBinding binding;

    CartProducts_Adapter cartProductsAdapter;

    List<Product_info> productInfoList = new ArrayList<>();

    ValueEventListener eventListenerForCart;
    DatabaseReference cartRef;
    Dialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCartScreenBinding.inflate(inflater, container, false);
        progressDialog = new Dialog(requireContext());


        cartRef = FirebaseUtil.getFirebaseDatabase()
                .getReference()
                .child("Cart")
                .child(FirebaseUtil.getFirebaseAuth().getUid());

        cartProductsAdapter = new CartProducts_Adapter(productInfoList, this::navigateWithInfo);
        binding.rv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        binding.rv.setAdapter(cartProductsAdapter);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.checkout.setOnClickListener(v->{
            findNavController(this).navigate(R.id.action_CartScreen_to_checkout_fragment);
        });

        eventListenerForCart = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productInfoList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Product_info productInfo = dataSnapshot.getValue(Product_info.class);
                    productInfoList.add(productInfo);
                }
                if (productInfoList.isEmpty()) {
                    changeEmptyVisibility();
                } else {
                    changeVisibility();
                    cartProductsAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        cartRef.addValueEventListener(eventListenerForCart);
    }

    private void changeVisibility() {
        binding.pg.setVisibility(View.INVISIBLE);
        binding.rv.setVisibility(View.VISIBLE);
        binding.checkout.setVisibility(View.VISIBLE);
    }

    private void changeEmptyVisibility() {
        binding.pg.setVisibility(View.INVISIBLE);
        binding.checkout.setVisibility(View.GONE);
        binding.cartEmpty.setVisibility(View.VISIBLE);
    }

    private void navigateWithInfo(Pair<Product_info, Integer> productInfoIntegerPair) {
        showProgressDialog();
        if (productInfoIntegerPair.second == R.id.plus) {
            showProgressDialog();
            String productId = productInfoIntegerPair.first.getItem_id();
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
                                })
                                .addOnFailureListener(e -> {
                                    dismissProgressDialog();
                                    Toast.makeText(getContext(), "Failed to update the quantity !", Toast.LENGTH_SHORT).show();
                                });
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    dismissProgressDialog();
                    Toast.makeText(getContext(), "Failed to read cart data!", Toast.LENGTH_SHORT).show();
                }
            });


        } else if (productInfoIntegerPair.second == R.id.minus) {

            showProgressDialog();
            String productId = productInfoIntegerPair.first.getItem_id();
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
                        int newQuantity = existingProductInfo.getSelectedQuantity() - 1;
                        cartRef.child(existingProductPushKey).child("selectedQuantity").setValue(newQuantity)
                                .addOnSuccessListener(unused -> {
                                    dismissProgressDialog();
                                })
                                .addOnFailureListener(e -> {
                                    dismissProgressDialog();
                                    Toast.makeText(getContext(), "Failed to update the quantity !", Toast.LENGTH_SHORT).show();
                                });
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    dismissProgressDialog();
                    Toast.makeText(getContext(), "Failed to read cart data!", Toast.LENGTH_SHORT).show();
                }
            });


        } else if (productInfoIntegerPair.second == R.id.clear_product) {
            cartRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String existingProductKey = null;

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Product_info cartProduct = snapshot.getValue(Product_info.class);
                        if (cartProduct != null && cartProduct.getItem_id().equals(productInfoIntegerPair.first.getItem_id())) {
                            existingProductKey = snapshot.getKey();
                            break;
                        }
                    }

                    if (existingProductKey != null) {
                        cartRef.child(existingProductKey).removeValue()
                                .addOnSuccessListener(unused -> {
                                    dismissProgressDialog();
                                    Toast.makeText(getContext(), "Removed from Cart Successfully", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    dismissProgressDialog();
                                    Toast.makeText(getContext(), "Failed to remove from the cart!", Toast.LENGTH_SHORT).show();
                                });
                    } else {
                        dismissProgressDialog();
                        Toast.makeText(getContext(), "Product not found in cart!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    dismissProgressDialog();
                    Toast.makeText(getContext(), "Failed to read cart data!", Toast.LENGTH_SHORT).show();
                }
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
}
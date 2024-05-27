package com.appdev.gadgetsgalaxy;

import static androidx.navigation.fragment.FragmentKt.findNavController;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.recyclerview.widget.GridLayoutManager;

import com.appdev.gadgetsgalaxy.data.Product_info;
import com.appdev.gadgetsgalaxy.databinding.FragmentProductShowcaseBinding;
import com.appdev.gadgetsgalaxy.recyclerview.Product_image_adapter;
import com.appdev.gadgetsgalaxy.utils.FirebaseUtil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class product_showcase extends Fragment {

    private FragmentProductShowcaseBinding productShowcaseBinding;
    Product_image_adapter product_image_adapter;

    List<Product_info> productInfoList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        productShowcaseBinding = FragmentProductShowcaseBinding.inflate(inflater, container, false);
        product_image_adapter = new Product_image_adapter(productInfoList, this::navigateWithInfo);
        productShowcaseBinding.rv.setLayoutManager(new GridLayoutManager(getContext(), 2));
        productShowcaseBinding.rv.setAdapter(product_image_adapter);
        productShowcaseBinding.backBtn.setOnClickListener(view -> {
            findNavController(this).popBackStack();
        });
        productShowcaseBinding.floatingButton.setOnClickListener(view -> {
            NavDirections action = product_showcaseDirections.actionProductShowcaseToProductEntry(null);
            findNavController(this).navigate(action);
        });
        productShowcaseBinding.catBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(requireContext(), view, 0, 0, R.style.PopupTheme);
                popupMenu.setOnMenuItemClickListener(menuItem -> {

                    String title = Objects.requireNonNull(menuItem.getTitle()).toString();
                    switch (title) {
                        case "All products":
                            // Handle "All products" click
                            return true;
                        case "Laptops":
                            // Handle "Laptops" click
                            return true;
                        case "SmartPhones":
                            // Handle "SmartPhones" click
                            return true;
                        case "GameConsoles":
                            // Handle "GameConsoles" click
                            return true;
                        case "Audio":
                            // Handle "Audio" click
                            return true;
                        default:
                            // Default case
                            return false;
                    }
                });
                popupMenu.inflate(R.menu.menu_category);
                popupMenu.show();
            }
        });
        return productShowcaseBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FirebaseUtil.getFirebaseDatabase().getReference().child("Products").addValueEventListener(new ValueEventListener() {
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
        });
    }

    private void changeVisibility() {
        productShowcaseBinding.rv.setVisibility(View.VISIBLE);
        productShowcaseBinding.progres.setVisibility(View.GONE);
    }

    private void navigateWithInfo(Product_info productInfo) {
        NavDirections action = product_showcaseDirections.actionProductShowcaseToProductInDetail(productInfo);
        findNavController(this).navigate(action);
    }

}
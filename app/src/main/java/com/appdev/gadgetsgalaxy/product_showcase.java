package com.appdev.gadgetsgalaxy;

import static androidx.navigation.fragment.FragmentKt.findNavController;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.recyclerview.widget.GridLayoutManager;

import com.appdev.gadgetsgalaxy.data.Category_info;
import com.appdev.gadgetsgalaxy.data.Product_info;
import com.appdev.gadgetsgalaxy.databinding.FragmentProductShowcaseBinding;
import com.appdev.gadgetsgalaxy.recyclerview.Product_image_adapter;
import com.appdev.gadgetsgalaxy.utils.FirebaseUtil;
import com.appdev.gadgetsgalaxy.utils.Utility;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class product_showcase extends Fragment {

    private FragmentProductShowcaseBinding productShowcaseBinding;
    Product_image_adapter product_image_adapter;

    List<Product_info> productInfoList = new ArrayList<>();
    List<Product_info> filteredProductInfoList = new ArrayList<>();
    List<String> categoryList = new ArrayList<>();
    ValueEventListener categoryItemListener;
    Menu menu;
    PopupMenu popupMenu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        productShowcaseBinding = FragmentProductShowcaseBinding.inflate(inflater, container, false);
        product_image_adapter = new Product_image_adapter(filteredProductInfoList, this::navigateWithInfo);
        checkAuth();
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
                popupMenu = new PopupMenu(requireContext(), view, 0, 0, R.style.PopupTheme);
                menu = popupMenu.getMenu();
                for (String category : categoryList) {
                    menu.add(category);
                }
                popupMenu.setOnMenuItemClickListener(menuItem -> {
                    String title = menuItem.getTitle().toString();
                    filteredProductInfoList.clear();
                    if (title == null || title.equals("All products")) {
                        filteredProductInfoList.addAll(productInfoList);
                    } else {
                        for (Product_info item : productInfoList) {
                            if (item.getCategory().equals(title)) {
                                filteredProductInfoList.add(item);
                            }
                        }
                    }
                    product_image_adapter.notifyDataSetChanged();
                    return true;
                });
                popupMenu.show();
            }
        });
        return productShowcaseBinding.getRoot();
    }

    private void checkAuth() {
        String userType = Utility.getUserTypeFromPrefs(productShowcaseBinding.getRoot().getContext());
        if (!userType.trim().isEmpty()) {
            if ("ADMIN".equals(userType)) {
                productShowcaseBinding.floatingButton.setVisibility(View.VISIBLE);
            } else if ("USER".equals(userType)) {
                productShowcaseBinding.floatingButton.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        categoryItemListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categoryList.clear();
                categoryList.add("All products");
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Category_info categoryInfo = dataSnapshot.getValue(Category_info.class);
                    categoryList.add(categoryInfo.getCatTitle());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };

        FirebaseUtil.getFirebaseDatabase().getReference()
                .child("Categories")
                .addValueEventListener(categoryItemListener);

        productShowcaseBinding.searchContentConcpt.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });


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
                filteredProductInfoList.clear();
                filteredProductInfoList.addAll(productInfoList);
                changeVisibility();
                product_image_adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void filter(String text) {
        filteredProductInfoList.clear();
        if (text.trim().isEmpty()) {
            filteredProductInfoList.addAll(productInfoList);
        } else {
            for (Product_info item : productInfoList) {
                if (item.getItem_name().toLowerCase().contains(text.toLowerCase())) {
                    filteredProductInfoList.add(item);
                }
            }
        }
        product_image_adapter.notifyDataSetChanged();
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
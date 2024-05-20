package com.appdev.gadgetsgalaxy;

import static androidx.navigation.fragment.FragmentKt.findNavController;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Pair;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.appdev.gadgetsgalaxy.data.Category_info;
import com.appdev.gadgetsgalaxy.data.Product_info;
import com.appdev.gadgetsgalaxy.databinding.FragmentCategoryInfoBinding;
import com.appdev.gadgetsgalaxy.databinding.FragmentProductShowcaseBinding;
import com.appdev.gadgetsgalaxy.recyclerview.Category_Image_Adapter;
import com.appdev.gadgetsgalaxy.recyclerview.Product_image_adapter;

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

        productInfoList.add(new Product_info("Iphone 15 pro max", "", 12999, 0, 1, 23, 4.4f));
        productInfoList.add(new Product_info("Iphone 15 pro max", "", 12999, 0, 2, 23, 4.4f));
        productInfoList.add(new Product_info("Iphone 15 pro max", "", 12999, 10000, 3, 23, 4.4f));
        productInfoList.add(new Product_info("Iphone 15 pro max", "", 12999, 0, 4, 23, 4.4f));
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
            findNavController(this).navigate(R.id.action_product_showcase_to_product_entry);
        });
        productShowcaseBinding.catBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(requireContext(), view,0,0,R.style.PopupTheme);
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

    private void navigateWithInfo(Product_info productInfo) {
        findNavController(this).navigate(R.id.action_product_showcase_to_product_in_detail);
    }

}
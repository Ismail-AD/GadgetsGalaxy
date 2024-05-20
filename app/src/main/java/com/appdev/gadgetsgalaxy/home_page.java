package com.appdev.gadgetsgalaxy;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.appdev.gadgetsgalaxy.data.Product_info;
import com.appdev.gadgetsgalaxy.databinding.FragmentHomePageBinding;
import com.appdev.gadgetsgalaxy.databinding.FragmentProductShowcaseBinding;
import com.appdev.gadgetsgalaxy.recyclerview.Chip_adapter;
import com.appdev.gadgetsgalaxy.recyclerview.Product_image_adapter;
import com.denzcoskun.imageslider.constants.AnimationTypes;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class home_page extends Fragment {
    FragmentHomePageBinding homePageBinding;
    Chip_adapter chipAdapter;
    Product_image_adapter product_image_adapter;

    ArrayList<String> filterList = new ArrayList<>();
    List<Product_info> productInfoList = new ArrayList<>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainActivity) requireActivity()).showBottomNavigationView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        homePageBinding = FragmentHomePageBinding.inflate(inflater, container, false);
        filterList.addAll(Arrays.asList("All", "Smartphones", "Laptops", "Playstation", "Accessories"));


        productInfoList.add(new Product_info("Iphone 15 pro max", "", 12999, 0, 1, 23, 4.4f));
        productInfoList.add(new Product_info("Iphone 15 pro max", "", 12999, 0, 2, 23, 4.4f));
        productInfoList.add(new Product_info("Iphone 15 pro max", "", 12999, 10000, 3, 23, 4.4f));
        productInfoList.add(new Product_info("Iphone 15 pro max", "", 12999, 0, 4, 23, 4.4f));


        product_image_adapter = new Product_image_adapter(productInfoList, this::navigateWithInfo);
        homePageBinding.rvProducts.setLayoutManager(new GridLayoutManager(getContext(), 2));
        homePageBinding.rvProducts.setAdapter(product_image_adapter);

        chipAdapter = new Chip_adapter(filterList, this::onFilterChange);
        homePageBinding.rv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        homePageBinding.rv.setAdapter(chipAdapter);

        ArrayList<SlideModel> imageList = new ArrayList<>(); // Create image list
        imageList.add(new SlideModel(R.drawable.sale1, ScaleTypes.FIT));
        imageList.add(new SlideModel(R.drawable.sale2, ScaleTypes.FIT));
        imageList.add(new SlideModel(R.drawable.sale3, ScaleTypes.FIT));

        homePageBinding.imageSliderHome.setImageList(imageList);
        homePageBinding.imageSliderHome.setSlideAnimation(AnimationTypes.ZOOM_IN);

        return homePageBinding.getRoot();
    }

    private void navigateWithInfo(Product_info productInfo) {
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void onFilterChange(String newChip) {

    }
}
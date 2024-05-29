package com.appdev.gadgetsgalaxy;

import static androidx.navigation.fragment.FragmentKt.findNavController;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.appdev.gadgetsgalaxy.data.Product_info;
import com.appdev.gadgetsgalaxy.databinding.FragmentHomePageBinding;
import com.appdev.gadgetsgalaxy.recyclerview.Chip_adapter;
import com.appdev.gadgetsgalaxy.recyclerview.Product_image_adapter;
import com.appdev.gadgetsgalaxy.utils.FirebaseUtil;
import com.appdev.gadgetsgalaxy.utils.Utility;
import com.denzcoskun.imageslider.constants.AnimationTypes;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class home_page extends Fragment {
    FragmentHomePageBinding homePageBinding;
    Chip_adapter chipAdapter;
    Product_image_adapter product_image_adapter;

    ArrayList<String> filterList = new ArrayList<>();
    List<Product_info> productInfoList = new ArrayList<>();
    ArrayList<SlideModel> imageListHome = new ArrayList<>();
    ArrayList<String> homeList = new ArrayList<>();
    ValueEventListener valueEventListenerForHome;
    ValueEventListener valueEventListenerForProducts;
    ValueEventListener categoryListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainActivity) requireActivity()).checkAvailability();
        if (Utility.isDarkModeActivated(requireActivity())) {
            Utility.status_bar_dark(requireActivity(), R.color.black);
        } else {
            Utility.status_bar(requireActivity(), R.color.white);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        homePageBinding = FragmentHomePageBinding.inflate(inflater, container, false);


        homePageBinding.gosrch.setOnClickListener(v->{
            findNavController(this).navigate(R.id.action_home_page_to_product_showcase);
        });
        product_image_adapter = new Product_image_adapter(productInfoList, this::navigateWithInfo);
        homePageBinding.rvProducts.setLayoutManager(new GridLayoutManager(getContext(), 2));
        homePageBinding.rvProducts.setAdapter(product_image_adapter);



        chipAdapter = new Chip_adapter(filterList, this::onFilterChange);
        homePageBinding.rv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        homePageBinding.rv.setAdapter(chipAdapter);

        homePageBinding.imageSliderHome.setSlideAnimation(AnimationTypes.FLIP_HORIZONTAL);


        return homePageBinding.getRoot();
    }

    private void navigateWithInfo(Product_info productInfo) {
        NavDirections action = home_pageDirections.actionHomePageToProductInDetail(productInfo);
        findNavController(this).navigate(action);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        valueEventListenerForHome = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                homeList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String fetchedUrl = dataSnapshot.getValue(String.class);
                    homeList.add(fetchedUrl);
                }
                setUpViewsForHome();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };

        FirebaseUtil.getFirebaseDatabase().getReference()
                .child("Banners").child("Home")
                .addValueEventListener(valueEventListenerForHome);

        categoryListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                filterList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String cat = dataSnapshot.child("catTitle").getValue(String.class);
                    filterList.add(cat);
                }
                if(!filterList.isEmpty()){
                    filterList.add(0, "All");
                }
                changeVisibilityOfChip();
                chipAdapter.notifyDataSetChanged();
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

        valueEventListenerForProducts = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productInfoList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Product_info product = snapshot.getValue(Product_info.class);
                    if (product != null) {
                        productInfoList.add(product);
                    }
                }
                if(!productInfoList.isEmpty()){
                    productInfoList.sort((p1, p2) -> Integer.compare(p2.getItem_discounted_price(), p1.getItem_discounted_price()));
                }
                changeVisibility();
                product_image_adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };
        FirebaseUtil.getFirebaseDatabase().getReference().child("Products").addValueEventListener(valueEventListenerForProducts);


    }

    private void changeVisibilityOfChip() {
        homePageBinding.rv.setVisibility(View.VISIBLE);
        homePageBinding.progresCat.setVisibility(View.GONE);
    }

    private void changeVisibility() {
        homePageBinding.rvProducts.setVisibility(View.VISIBLE);
        homePageBinding.progres.setVisibility(View.GONE);
    }

    private void setUpViewsForHome() {
        imageListHome.clear();
        for (String url : homeList) {
            imageListHome.add(new SlideModel(url, ScaleTypes.FIT));
        }
        homePageBinding.imageSliderHome.setImageList(imageListHome);
        if (homeList.isEmpty()) {
            hideHomeCard();
        } else {
            showHomeCard();
        }
    }

    public void hideHomeCard() {
        homePageBinding.bannerHome.setVisibility(View.GONE);
    }


    public void showHomeCard() {
        homePageBinding.bannerHome.setVisibility(View.VISIBLE);
    }

    public void onFilterChange(String newChip) {

    }
}
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
import androidx.recyclerview.widget.GridLayoutManager;

import com.appdev.gadgetsgalaxy.data.Category_info;
import com.appdev.gadgetsgalaxy.databinding.FragmentSeeAllCategoriesBinding;
import com.appdev.gadgetsgalaxy.recyclerview.Category_Image_Adapter;
import com.appdev.gadgetsgalaxy.utils.FirebaseUtil;
import com.denzcoskun.imageslider.constants.AnimationTypes;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class See_all_categories extends Fragment {

    FragmentSeeAllCategoriesBinding binding;
    Category_Image_Adapter categoryImageAdapter;

    List<Category_info> categoryInfoList = new ArrayList<>();
    ArrayList<String> catList = new ArrayList<>();
    ValueEventListener valueEventListenerForCat;
    ValueEventListener categoryItemListener;

    ArrayList<SlideModel> imageListCat = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentSeeAllCategoriesBinding.inflate(inflater, container, false);
        categoryImageAdapter = new Category_Image_Adapter(categoryInfoList, this::navigateWithInfo);
        binding.rv.setLayoutManager(new GridLayoutManager(getContext(), 2));
        binding.rv.setAdapter(categoryImageAdapter);

        binding.imageSliderHome.setSlideAnimation(AnimationTypes.FLIP_HORIZONTAL);

        return binding.getRoot();
    }

    private void navigateWithInfo(Category_info categoryInfoImageViewPair) {
        findNavController(this).navigate(R.id.action_see_all_categories_to_categoryIndetail);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        valueEventListenerForCat = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                catList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String fetchedUrl = dataSnapshot.getValue(String.class);
                    catList.add(fetchedUrl);
                }
                setUpViewsForCat();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };
        FirebaseUtil.getFirebaseDatabase().getReference()
                .child("Banners").child("Category")
                .addValueEventListener(valueEventListenerForCat);


        // for items
        categoryItemListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Category_info categoryInfo = dataSnapshot.getValue(Category_info.class);
                    categoryInfoList.add(categoryInfo);
                }
                changeVisibility();
                categoryImageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };

        // Attach the ValueEventListener to the "Categories" node
        FirebaseUtil.getFirebaseDatabase().getReference()
                .child("Categories")
                .addValueEventListener(categoryItemListener);

    }

    private void changeVisibility() {
        binding.pg.setVisibility(View.INVISIBLE);
        binding.rv.setVisibility(View.VISIBLE);
    }

    private void setUpViewsForCat() {
        imageListCat.clear();
        for (String url : catList) {
            imageListCat.add(new SlideModel(url, ScaleTypes.FIT));
        }
        binding.imageSliderHome.setImageList(imageListCat);
        if (catList.isEmpty()) {
            binding.bannerHome.setVisibility(View.GONE);
        } else {
            binding.bannerHome.setVisibility(View.VISIBLE);
        }
    }
}
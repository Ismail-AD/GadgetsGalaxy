package com.appdev.gadgetsgalaxy;

import static androidx.navigation.fragment.FragmentKt.findNavController;

import android.os.Bundle;

import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.FragmentNavigator;
import androidx.navigation.fragment.FragmentNavigatorExtrasKt;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.appdev.gadgetsgalaxy.data.Category_info;
import com.appdev.gadgetsgalaxy.data.User_info;
import com.appdev.gadgetsgalaxy.databinding.AddCategoryBinding;
import com.appdev.gadgetsgalaxy.databinding.FragmentCategoryInfoBinding;
import com.appdev.gadgetsgalaxy.databinding.UserInfoDialogBinding;
import com.appdev.gadgetsgalaxy.recyclerview.Category_Image_Adapter;
import com.appdev.gadgetsgalaxy.recyclerview.Customer_info_adapter;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class categoryInfo extends Fragment {
    private FragmentCategoryInfoBinding categoryInfoBinding;
    Category_Image_Adapter categoryImageAdapter;

    List<Category_info> categoryInfoList = new ArrayList<>();
    AddCategoryBinding addCategoryBinding;
    BottomSheetDialog bottomSheetDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        categoryInfoList.add(new Category_info("PlayStations", "", "NothingNew"));
        categoryInfoList.add(new Category_info("Mobile Phones", "", "NothingNew"));
        categoryInfoList.add(new Category_info("Accessories", "", "NothingNew"));
        categoryInfoList.add(new Category_info("Laptops", "", "NothingNew"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        categoryInfoBinding = FragmentCategoryInfoBinding.inflate(inflater, container, false);

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
            bottomSheetDialog.setContentView(addCategoryBinding.getRoot());
            Objects.requireNonNull(bottomSheetDialog.getWindow()).setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            bottomSheetDialog.setCancelable(false);
            bottomSheetDialog.show();
        });

        return categoryInfoBinding.getRoot();
    }

    private void navigateWithInfo(Pair<Category_info, ImageView> categoryInfoImageViewPair) {
        FragmentNavigator.Extras extras = new FragmentNavigator.Extras.Builder().addSharedElement(categoryInfoImageViewPair.second,categoryInfoImageViewPair.first.getCatTitle()).build();
        findNavController(this).navigate(R.id.action_categoryInfo_to_categoryIndetail);
    }

}
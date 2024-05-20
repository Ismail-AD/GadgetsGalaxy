package com.appdev.gadgetsgalaxy;

import static androidx.navigation.fragment.FragmentKt.findNavController;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.appdev.gadgetsgalaxy.databinding.AddCategoryBinding;
import com.appdev.gadgetsgalaxy.databinding.FragmentCategoryIndetailBinding;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Objects;
import java.util.concurrent.TimeUnit;


public class categoryIndetail extends Fragment {

    FragmentCategoryIndetailBinding categoryInDetailBinding;
    AddCategoryBinding addCategoryBinding;
    BottomSheetDialog bottomSheetDialog;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        categoryInDetailBinding = FragmentCategoryIndetailBinding.inflate(inflater, container, false);
        setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));


        categoryInDetailBinding.floatingButton.setOnClickListener(view -> {
            bottomSheetDialog = new BottomSheetDialog(
                    requireContext());
            addCategoryBinding = AddCategoryBinding.inflate(getLayoutInflater());
            addCategoryBinding.closeBottomSheet.setOnClickListener(view1 -> bottomSheetDialog.dismiss());
            bottomSheetDialog.setContentView(addCategoryBinding.getRoot());

            ViewGroup.LayoutParams layoutParams = addCategoryBinding.image.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            addCategoryBinding.image.setLayoutParams(layoutParams);
            addCategoryBinding.image.setScaleType(ImageView.ScaleType.FIT_XY);


            Glide.with(addCategoryBinding.image).load("url").placeholder(R.drawable.playstation).into(addCategoryBinding.image);
            addCategoryBinding.descData.setText("HELLO OKAY");
            addCategoryBinding.titlefield.setText("HELLO");
            addCategoryBinding.loginBtn.setText("Update category");
            Objects.requireNonNull(bottomSheetDialog.getWindow()).setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            bottomSheetDialog.setCancelable(false);
            bottomSheetDialog.show();
        });

        return categoryInDetailBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        categoryInDetailBinding.backBtn.setOnClickListener(view1 -> {
            findNavController(this).popBackStack();
        });

    }
}
package com.appdev.gadgetsgalaxy;

import static androidx.navigation.fragment.FragmentKt.findNavController;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.appdev.gadgetsgalaxy.databinding.AddCategoryBinding;
import com.appdev.gadgetsgalaxy.databinding.BannerCreationBinding;
import com.appdev.gadgetsgalaxy.databinding.FragmentBannersAdminBinding;
import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.constants.AnimationTypes;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.Objects;


public class Banners_admin extends Fragment {
    FragmentBannersAdminBinding binding;
    BannerCreationBinding creationBinding;
    BottomSheetDialog bottomSheetDialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBannersAdminBinding.inflate(inflater,container,false);
        ArrayList<SlideModel> imageList = new ArrayList<>(); // Create image list

        imageList.add(new SlideModel(R.drawable.sale1,ScaleTypes.FIT));
        imageList.add(new SlideModel(R.drawable.sale2, ScaleTypes.FIT));
        imageList.add(new SlideModel(R.drawable.sale3, ScaleTypes.FIT));

        binding.imageSliderHome.setImageList(imageList);
        binding.imageSliderHome.setSlideAnimation(AnimationTypes.ZOOM_IN);
        binding.imageSliderCat.setSlideAnimation(AnimationTypes.ZOOM_IN);

        binding.imageSliderCat.setImageList(imageList);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.backBtn.setOnClickListener(view1 -> {
            findNavController(this).popBackStack();
        });

        binding.floatingButton.setOnClickListener(v->{
            bottomSheetDialog = new BottomSheetDialog(
                    requireContext());
            creationBinding = BannerCreationBinding.inflate(getLayoutInflater());
            creationBinding.closeBottomSheet.setOnClickListener(view1 -> bottomSheetDialog.dismiss());
            bottomSheetDialog.setContentView(creationBinding.getRoot());

            //After user add'san image
//            ViewGroup.LayoutParams layoutParams = creationBinding.image.getLayoutParams();
//            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
//            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
//            creationBinding.image.setLayoutParams(layoutParams);
//            creationBinding.image.setScaleType(ImageView.ScaleType.CENTER);

//            int genderRadioButtonId = creationBinding.dailyWeeklyButtonView.getCheckedRadioButtonId();
//            RadioButton genderRadioButton = requireActivity().findViewById(genderRadioButtonId);
            Objects.requireNonNull(bottomSheetDialog.getWindow()).setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            bottomSheetDialog.setCancelable(false);
            bottomSheetDialog.show();

        });

    }
}
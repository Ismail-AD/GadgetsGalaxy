package com.appdev.gadgetsgalaxy;

import static androidx.navigation.fragment.FragmentKt.findNavController;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.appdev.gadgetsgalaxy.databinding.FragmentProductInDetailBinding;
import com.bumptech.glide.Glide;


public class product_in_detail extends Fragment {
    FragmentProductInDetailBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProductInDetailBinding.inflate(inflater, container, false);
        binding.floatingButton.setOnClickListener(v -> {
            findNavController(this).navigate(R.id.action_product_in_detail_to_product_entry);
        });
        binding.backBtn.setOnClickListener(view1 -> {
            findNavController(this).popBackStack();
        });

        return binding.getRoot();
    }
}
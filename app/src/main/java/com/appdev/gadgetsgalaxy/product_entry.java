package com.appdev.gadgetsgalaxy;

import static androidx.navigation.fragment.FragmentKt.findNavController;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appdev.gadgetsgalaxy.databinding.FragmentProductEntryBinding;


public class product_entry extends Fragment {

    FragmentProductEntryBinding productEntryBinding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        productEntryBinding = FragmentProductEntryBinding.inflate(inflater,container,false);
        productEntryBinding.backBtn.setOnClickListener(view -> {
            findNavController(this).popBackStack();
        });

        return productEntryBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        productEntryBinding.onSale.setOnClickListener(view1 -> {
            if(productEntryBinding.onSale.isChecked()){
                productEntryBinding.sales.setVisibility(View.VISIBLE);
            } else{
                productEntryBinding.sales.setVisibility(View.INVISIBLE);
            }
        });
    }
}
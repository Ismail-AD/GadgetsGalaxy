package com.appdev.gadgetsgalaxy;

import static androidx.navigation.fragment.FragmentKt.findNavController;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.recyclerview.widget.GridLayoutManager;

import com.appdev.gadgetsgalaxy.data.Product_info;
import com.appdev.gadgetsgalaxy.databinding.FragmentMyWishListBinding;
import com.appdev.gadgetsgalaxy.recyclerview.Product_image_adapter;
import com.appdev.gadgetsgalaxy.utils.FirebaseUtil;
import com.appdev.gadgetsgalaxy.utils.Utility;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class myWishList extends Fragment {
    FragmentMyWishListBinding binding;
    Product_image_adapter product_image_adapter;

    List<Product_info> productInfoList = new ArrayList<>();
    ValueEventListener eventListenerForWishlist;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Utility.isDarkModeActivated(requireActivity())) {
            Utility.status_bar_dark(requireActivity(), R.color.black);
        } else {
            Utility.status_bar(requireActivity(), R.color.white);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMyWishListBinding.inflate(inflater, container, false);

        product_image_adapter = new Product_image_adapter(productInfoList, this::navigateWithInfo);
        binding.rv.setLayoutManager(new GridLayoutManager(getContext(), 2));
        binding.rv.setAdapter(product_image_adapter);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        eventListenerForWishlist = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productInfoList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Product_info productInfo = dataSnapshot.getValue(Product_info.class);
                    productInfoList.add(productInfo);
                }
                if(productInfoList.isEmpty()){
                    changeEmptyVisibility();
                } else{
                    changeVisibility();
                    product_image_adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        FirebaseUtil.getFirebaseDatabase()
                .getReference()
                .child("Wishlist")
                .child(FirebaseUtil.getFirebaseAuth().getUid()).addValueEventListener(eventListenerForWishlist);
    }

    private void changeEmptyVisibility() {
        binding.pg.setVisibility(View.INVISIBLE);
        binding.wlistEmpty.setVisibility(View.VISIBLE);
    }

    private void changeVisibility() {
        binding.pg.setVisibility(View.INVISIBLE);
        binding.rv.setVisibility(View.VISIBLE);
    }

    private void navigateWithInfo(Product_info productInfo) {
        NavDirections action = myWishListDirections.actionMyWishListToProductInDetail(productInfo);
        findNavController(this).navigate(action);
    }
}
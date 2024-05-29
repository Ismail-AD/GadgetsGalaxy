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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.appdev.gadgetsgalaxy.data.Order_info;
import com.appdev.gadgetsgalaxy.databinding.FragmentUserSideOrdersBinding;
import com.appdev.gadgetsgalaxy.recyclerview.OrderInfoAdapter;
import com.appdev.gadgetsgalaxy.utils.FirebaseUtil;
import com.appdev.gadgetsgalaxy.utils.Utility;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class user_side_orders extends Fragment {

    FragmentUserSideOrdersBinding binding;
    OrderInfoAdapter adapter;
    List<List<Order_info>> allUserOrders = new ArrayList<>();

    ValueEventListener eventListener;

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
        binding = FragmentUserSideOrdersBinding.inflate(inflater, container, false);
        adapter = new OrderInfoAdapter(allUserOrders);
        binding.rv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        binding.rv.setAdapter(adapter);

        binding.backBtn.setOnClickListener(v -> {
            findNavController(this).popBackStack();
        });
        return binding.getRoot();
    }


    public void orderIsClicked(Order_info orderInfo) {

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                allUserOrders.clear();
                for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                    List<Order_info> orderInfoList = new ArrayList<>();
                    for (DataSnapshot productSnapshot : orderSnapshot.getChildren()) {
                        Order_info orderInfo = productSnapshot.getValue(Order_info.class);
                        if (orderInfo != null) {
                            orderInfoList.add(orderInfo);
                        }
                    }
                    allUserOrders.add(orderInfoList);
                }
                changeVisibility();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Failed to load orders: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };
        FirebaseUtil.getFirebaseDatabase().getReference().child("orders")
                .child(FirebaseUtil.getFirebaseAuth().getUid())
                .addListenerForSingleValueEvent(eventListener);
    }

    private void changeVisibility() {
        binding.pg.setVisibility(View.GONE);
        binding.rv.setVisibility(View.VISIBLE);
    }
}
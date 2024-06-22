package com.appdev.gadgetsgalaxy;

import static androidx.navigation.fragment.FragmentKt.findNavController;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.appdev.gadgetsgalaxy.data.Order_info;
import com.appdev.gadgetsgalaxy.databinding.FragmentSentOrCancelOrdersBinding;
import com.appdev.gadgetsgalaxy.recyclerview.Order_info_adapter_admin;
import com.appdev.gadgetsgalaxy.utils.FirebaseUtil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class sent_or_cancel_orders extends Fragment {

    FragmentSentOrCancelOrdersBinding binding;
    Order_info_adapter_admin adapter;
    List<List<Order_info>> allUserOrders = new ArrayList<>();
    Dialog progressDialog;

    String parentNode = "", arg;
    ValueEventListener eventListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSentOrCancelOrdersBinding.inflate(inflater, container, false);

        progressDialog = new Dialog(requireContext());
        arg = sent_or_cancel_ordersArgs.fromBundle(getArguments()).getOrderState();
        setupParentNode();

        adapter = new Order_info_adapter_admin(allUserOrders, this::clickedOrder);
        binding.rv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        binding.rv.setAdapter(adapter);
        binding.backBtn.setOnClickListener(view -> {
            findNavController(this).popBackStack();
        });
        return binding.getRoot();
    }

    private void setupParentNode() {
        if (arg.equals("sent")) {
            parentNode = "sentOrders";
            binding.titleTextView.setText("Orders Sent");
        } else if (arg.equals("cancel")) {
            parentNode = "cancelOrders";
            binding.titleTextView.setText("Canceled Orders");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                allUserOrders.clear();

                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        List<Order_info> orderInfoList = new ArrayList<>();

                        for (DataSnapshot productSnapshot : userSnapshot.getChildren()) {
                            Order_info orderInfo = productSnapshot.getValue(Order_info.class);
                            if (orderInfo != null) {
                                orderInfoList.add(orderInfo);
                            }
                        }
                        if (!orderInfoList.isEmpty()) {
                            allUserOrders.add(orderInfoList);
                        }
                }
                adapter.notifyDataSetChanged();
                changeVisibility();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Failed to load orders: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };
        FirebaseUtil.getFirebaseDatabase().getReference().child(parentNode)
                .addValueEventListener(eventListener);
    }

    private void changeVisibility() {
        binding.pg.setVisibility(View.GONE);
        if (allUserOrders.isEmpty()) {
            binding.rv.setVisibility(View.GONE);
            binding.ordersEmpty.setVisibility(View.VISIBLE);
        } else {
            binding.rv.setVisibility(View.VISIBLE);
            binding.ordersEmpty.setVisibility(View.GONE);
        }
    }

    private void clickedOrder(Pair<String, List<Order_info>> stringListPair) {
        // both sent and cancel orders are view-only
    }

}
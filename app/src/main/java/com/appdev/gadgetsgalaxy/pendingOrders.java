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
import com.appdev.gadgetsgalaxy.databinding.FragmentPendingOrdersBinding;
import com.appdev.gadgetsgalaxy.recyclerview.Order_info_adapter_admin;
import com.appdev.gadgetsgalaxy.utils.FirebaseUtil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class pendingOrders extends Fragment {
    FragmentPendingOrdersBinding pendingOrdersBinding;
    Order_info_adapter_admin adapter;
    List<List<Order_info>> allUserOrders = new ArrayList<>();
    Dialog progressDialog;

    ValueEventListener eventListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        pendingOrdersBinding = FragmentPendingOrdersBinding.inflate(inflater, container, false);
        progressDialog = new Dialog(requireContext());

        adapter = new Order_info_adapter_admin(allUserOrders, this::clickedOrder);
        pendingOrdersBinding.rv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        pendingOrdersBinding.rv.setAdapter(adapter);
        pendingOrdersBinding.backBtn.setOnClickListener(view -> {
            findNavController(this).popBackStack();
        });
        return pendingOrdersBinding.getRoot();
    }

    private void clickedOrder(Pair<String, List<Order_info>> stringListPair) {
        showProgressDialog();
        Order_info orderInfo = stringListPair.second.get(0);
        String orderId = orderInfo.getOrderId();
        DatabaseReference databaseReferenceOfOrders = FirebaseUtil.getFirebaseDatabase().getReference("pendingOrders").child(orderInfo.getUserId());
        DatabaseReference databaseReferenceOfUser = FirebaseUtil.getFirebaseDatabase().getReference("orders").child(orderInfo.getUserId());
        if (Objects.equals(stringListPair.first, "cancel")) {

            DatabaseReference databaseReference = FirebaseUtil.getFirebaseDatabase().getReference("cancelOrders");
            List<Order_info> updatedOrderList = new ArrayList<>();
            for (Order_info order : stringListPair.second) {
                order.setStatus("CANCELED");
                updatedOrderList.add(order);
            }
            databaseReference.child(orderId).setValue(updatedOrderList).addOnSuccessListener(aVoid -> {
                        databaseReferenceOfUser.child(orderId).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                                        Order_info order = orderSnapshot.getValue(Order_info.class);
                                        if (order != null) {
                                            orderSnapshot.getRef().child("status").setValue("CANCELED");
                                        }
                                    }
                                } else {

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                // Handle onCancelled event
                            }
                        });
                        databaseReferenceOfOrders.child(orderId).removeValue().addOnCompleteListener(task -> {
                            dismissProgressDialog();
                            if (task.isSuccessful()) {
                                Toast.makeText(requireContext(), "Order has been Canceled !", Toast.LENGTH_SHORT).show();
                            }
                        });
                    })
                    .addOnFailureListener(e -> {
                        dismissProgressDialog();
                        Toast.makeText(requireContext(), "Failed to cancel order.", Toast.LENGTH_SHORT).show();
                    });
        } else if (Objects.equals(stringListPair.first, "send")) {
            DatabaseReference databaseReference = FirebaseUtil.getFirebaseDatabase().getReference("sentOrders");
            List<Order_info> updatedNewOrderList = new ArrayList<>();
            for (Order_info order : stringListPair.second) {
                order.setStatus("SENT");
                updatedNewOrderList.add(order);
            }
            databaseReference.child(orderId).setValue(updatedNewOrderList).addOnSuccessListener(aVoid -> {
                        databaseReferenceOfUser.child(orderId).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                                        Order_info order = orderSnapshot.getValue(Order_info.class);
                                        if (order != null) {
                                            orderSnapshot.getRef().child("status").setValue("SENT");
                                        }
                                    }
                                } else {

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                // Handle onCancelled event
                            }
                        });
                        databaseReferenceOfOrders.child(orderId).removeValue().addOnCompleteListener(task -> {
                            dismissProgressDialog();
                            if (task.isSuccessful()) {
                                Toast.makeText(requireContext(), "Order has been Sent !", Toast.LENGTH_SHORT).show();
                            }
                        });
                    })
                    .addOnFailureListener(e -> {
                        dismissProgressDialog();
                        Toast.makeText(requireContext(), "Failed to send order.", Toast.LENGTH_SHORT).show();
                    });
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
                    for (DataSnapshot orderSnapshot : userSnapshot.getChildren()) {
                        List<Order_info> orderInfoList = new ArrayList<>();

                        for (DataSnapshot productSnapshot : orderSnapshot.getChildren()) {
                            Order_info orderInfo = productSnapshot.getValue(Order_info.class);
                            if (orderInfo != null) {
                                orderInfoList.add(orderInfo);
                            }
                        }
                        if (!orderInfoList.isEmpty()) {
                            allUserOrders.add(orderInfoList);
                        }
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

        FirebaseUtil.getFirebaseDatabase().getReference().child("pendingOrders")
                .addValueEventListener(eventListener);
    }

    public void showProgressDialog() {
        progressDialog.setContentView(R.layout.progress_bar);
        Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void changeVisibility() {
        pendingOrdersBinding.pg.setVisibility(View.GONE);
        if (allUserOrders.isEmpty()) {
            pendingOrdersBinding.rv.setVisibility(View.GONE);
            pendingOrdersBinding.orderEmpty.setVisibility(View.VISIBLE);
        } else {
            pendingOrdersBinding.rv.setVisibility(View.VISIBLE);
            pendingOrdersBinding.orderEmpty.setVisibility(View.GONE);
        }
    }
}


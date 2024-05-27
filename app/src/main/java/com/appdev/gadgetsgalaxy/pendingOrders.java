package com.appdev.gadgetsgalaxy;

import static androidx.navigation.fragment.FragmentKt.findNavController;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.appdev.gadgetsgalaxy.data.Order_info;
import com.appdev.gadgetsgalaxy.databinding.FragmentPendingOrdersBinding;
import com.appdev.gadgetsgalaxy.recyclerview.Orders_list_adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class pendingOrders extends Fragment {
    FragmentPendingOrdersBinding pendingOrdersBinding;
    Orders_list_adapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        pendingOrdersBinding = FragmentPendingOrdersBinding.inflate(inflater, container, false);

        adapter = new Orders_list_adapter(createUsersOrdersMap());
        pendingOrdersBinding.rv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        pendingOrdersBinding.rv.setAdapter(adapter);
        pendingOrdersBinding.backBtn.setOnClickListener(view -> {
            findNavController(this).popBackStack();
        });
        return pendingOrdersBinding.getRoot();
    }

    public static Map<String, List<Order_info>> createUsersOrdersMap() {
        Map<String, List<Order_info>> usersOrders = new HashMap<>();

        // Dummy orders for user 1
        List<Order_info> ordersUser1 = new ArrayList<>();
        ordersUser1.add(new Order_info("2024-05-17", 100.0f, "user1", "order1", "image1", "Delivered", "Product 1", 1, 10.0f, "Name 1", "email1@example.com", "123456789"));
        ordersUser1.add(new Order_info("2024-05-18", 200.0f, "user1", "order1", "image2", "Pending", "Product 2", 2, 10.0f, "Name 1", "email1@example.com", "123456789"));
        usersOrders.put("order1", ordersUser1);

        // Dummy orders for user 2
        List<Order_info> ordersUser2 = new ArrayList<>();
        ordersUser2.add(new Order_info("2024-05-19", 150.0f, "user2", "order3", "image3", "Delivered", "Product 3", 1, 10.0f, "Name 2", "email2@example.com", "123456789"));
        ordersUser2.add(new Order_info("2024-05-20", 250.0f, "user2", "order3", "image4", "Delivered", "Product 4", 2, 10.0f, "Name 2", "email2@example.com", "123456789"));
        usersOrders.put("order3", ordersUser2);

        // Dummy orders for user 3
        List<Order_info> ordersUser3 = new ArrayList<>();
        ordersUser3.add(new Order_info("2024-05-21", 180.0f, "user3", "order5", "image5", "Pending", "Product 5", 1, 10.0f, "Name 3", "email3@example.com", "123456789"));
        usersOrders.put("order5", ordersUser3);

        return usersOrders;
    }
}


package com.appdev.gadgetsgalaxy.recyclerview;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appdev.gadgetsgalaxy.data.Order_info;
import com.appdev.gadgetsgalaxy.databinding.UserOrderNumberBinding;

import java.util.List;
import java.util.function.Consumer;

public class Order_info_adapter_admin extends RecyclerView.Adapter<Order_info_adapter_admin.OrderViewHolder> {

    private List<List<Order_info>> allUserOrders;
    final Consumer<Pair<String,List<Order_info>>> clickedOrder;


    public Order_info_adapter_admin(List<List<Order_info>> allUserOrders, Consumer<Pair<String, List<Order_info>>> clickedOrder) {
        this.allUserOrders = allUserOrders;
        this.clickedOrder = clickedOrder;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        UserOrderNumberBinding binding = UserOrderNumberBinding.inflate(inflater, parent, false);
        return new OrderViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        List<Order_info> orderInfoList = allUserOrders.get(position);
        Orders_list_adapter ordersListAdapter = new Orders_list_adapter(orderInfoList, clickedOrder);
        holder.binding.rvparent.setLayoutManager(new LinearLayoutManager(holder.binding.getRoot().getContext()));
        holder.binding.rvparent.setAdapter(ordersListAdapter);
    }

    @Override
    public int getItemCount() {
        return allUserOrders.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        UserOrderNumberBinding binding;

        public OrderViewHolder(@NonNull UserOrderNumberBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}

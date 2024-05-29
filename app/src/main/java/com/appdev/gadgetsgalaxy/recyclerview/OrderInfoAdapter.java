package com.appdev.gadgetsgalaxy.recyclerview;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appdev.gadgetsgalaxy.data.Order_info;
import com.appdev.gadgetsgalaxy.databinding.UserOrderNumberBinding;

import java.util.List;

public class OrderInfoAdapter extends RecyclerView.Adapter<OrderInfoAdapter.OrderViewHolder> {

    private List<List<Order_info>> allUserOrders;

    public OrderInfoAdapter(List<List<Order_info>> allUserOrders) {
        this.allUserOrders = allUserOrders;
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
        orders_created_adapter productAdapter = new orders_created_adapter(orderInfoList);
        holder.binding.rvparent.setLayoutManager(new LinearLayoutManager(holder.binding.getRoot().getContext()));
        holder.binding.rvparent.setAdapter(productAdapter);
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

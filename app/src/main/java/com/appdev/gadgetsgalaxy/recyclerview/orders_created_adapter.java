package com.appdev.gadgetsgalaxy.recyclerview;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appdev.gadgetsgalaxy.data.Order_info;
import com.appdev.gadgetsgalaxy.databinding.OrderCreatedLayoutBinding;

import java.util.List;

public class orders_created_adapter extends RecyclerView.Adapter<orders_created_adapter.orderInfoViewHolder> {


    List<Order_info> orderList;

    public orders_created_adapter(List<Order_info> DataList) {
        this.orderList = DataList;
    }


    @NonNull
    @Override
    public orderInfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        OrderCreatedLayoutBinding orderCreatedLayoutBinding = OrderCreatedLayoutBinding.inflate(layoutInflater, parent, false);
        return new orderInfoViewHolder(orderCreatedLayoutBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull orderInfoViewHolder holder, int position) {
        Order_info orderInfo = orderList.get(position);
        holder.binding.setOrderData(orderInfo);
        holder.binding.executePendingBindings();
        int value;
        if (orderInfo.getItem_discounted_price() > 0) {
            value = orderInfo.getItem_discounted_price() * orderInfo.getSelectedQuantity();
        } else {
            value = orderInfo.getItem_price() * orderInfo.getSelectedQuantity();
        }
        holder.binding.price.setText(String.valueOf(value));
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }


    public static class orderInfoViewHolder extends RecyclerView.ViewHolder {

        OrderCreatedLayoutBinding binding;

        public orderInfoViewHolder(@NonNull OrderCreatedLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }
}

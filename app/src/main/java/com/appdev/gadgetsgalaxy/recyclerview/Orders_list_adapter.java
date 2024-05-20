package com.appdev.gadgetsgalaxy.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appdev.gadgetsgalaxy.R;
import com.appdev.gadgetsgalaxy.data.Order_info;
import com.appdev.gadgetsgalaxy.databinding.OrderInfoLayoutBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Orders_list_adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int ORDER_VIEW_TYPE = 1;
    private static final int DIVIDER_VIEW_TYPE = 2;

    private List<Object> items = new ArrayList<>();

    public Orders_list_adapter(Map<String, List<Order_info>> usersOrders) {
        int entryIndex = 0;
        int totalEntries = usersOrders.size();
        for (Map.Entry<String, List<Order_info>> entry : usersOrders.entrySet()) {
            items.addAll(entry.getValue()); // Add user's orders
            if (entryIndex < totalEntries - 1) {
                items.add(entry.getKey()); // Add order ID as divider item
            }
            entryIndex++;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == ORDER_VIEW_TYPE) {
            OrderInfoLayoutBinding binding = OrderInfoLayoutBinding.inflate(inflater, parent, false);
            return new OrderViewHolder(binding);
        } else if (viewType == DIVIDER_VIEW_TYPE) {
            View dividerView = inflater.inflate(R.layout.divider_seprator, parent, false);
            return new DividerViewHolder(dividerView);
        }
        throw new IllegalArgumentException("Invalid view type");
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Object item = items.get(position);
        if (item instanceof Order_info) {
            Order_info order = (Order_info) item;
            ((OrderViewHolder) holder).bind(order);
        } else if (item instanceof String) {

        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        Object item = items.get(position);
        if (item instanceof Order_info) {
            return ORDER_VIEW_TYPE;
        } else if (item instanceof String) {
            return DIVIDER_VIEW_TYPE;
        }
        throw new IllegalArgumentException("Invalid item type");
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {

        private final OrderInfoLayoutBinding binding;

        OrderViewHolder(@NonNull OrderInfoLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Order_info order) {
            binding.setOrderData(order);
            binding.executePendingBindings();
        }
    }

    public static class DividerViewHolder extends RecyclerView.ViewHolder {
        public DividerViewHolder(View itemView) {
            super(itemView);
        }
    }
}



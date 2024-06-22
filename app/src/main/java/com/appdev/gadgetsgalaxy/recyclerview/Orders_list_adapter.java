package com.appdev.gadgetsgalaxy.recyclerview;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.appdev.gadgetsgalaxy.R;
import com.appdev.gadgetsgalaxy.data.Order_info;
import com.appdev.gadgetsgalaxy.databinding.OrderInfoLayoutBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Orders_list_adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int ORDER_VIEW_TYPE = 1;
    private static final int DIVIDER_VIEW_TYPE = 2;

    public static Boolean isPending = true;
    private List<Object> items = new ArrayList<>();
    public List<Order_info> arrivedOrders = new ArrayList<>();
    final Consumer<Pair<String, List<Order_info>>> clickedOrder;


    public Orders_list_adapter(List<Order_info> orders, Consumer<Pair<String, List<Order_info>>> clickedOrder) {
        this.clickedOrder = clickedOrder;
        this.arrivedOrders = orders;
        String lastOrderId = null;
        for (Order_info orderInfo : orders) {
            items.add(orderInfo);
            lastOrderId = orderInfo.getOrderId();
        }
        isPending = orders.get(0).getStatus().equals("PENDING");
        if (lastOrderId != null) {
            items.add(lastOrderId); // Add the last order ID as divider item
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
            return new DividerViewHolder(dividerView, clickedOrder, arrivedOrders);
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
            // Handle divider binding if necessary
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
            int value;
            if (order.getItem_discounted_price() > 0) {
                value = order.getItem_discounted_price() * order.getSelectedQuantity();
            } else {
                value = order.getItem_price() * order.getSelectedQuantity();
            }
            String tPrice = "Rs:" + value;
            binding.pricetotal.setText(tPrice);
        }
    }

    public static class DividerViewHolder extends RecyclerView.ViewHolder {
        public DividerViewHolder(View itemView, Consumer<Pair<String, List<Order_info>>> clickedOrder, List<Order_info> arrivedOrders) {
            super(itemView);
            LinearLayout layout;
            AppCompatButton cancel;
            AppCompatButton accept;
            layout = itemView.getRootView().findViewById(R.id.both_btns);
            cancel = itemView.getRootView().findViewById(R.id.cancel);
            accept = itemView.getRootView().findViewById(R.id.send);
            if (isPending) {
                layout.setVisibility(View.VISIBLE);
            } else {
                layout.setVisibility(View.GONE);
            }
            cancel.setOnClickListener(v1 -> {
                clickedOrder.accept(new Pair<>("cancel", arrivedOrders));
            });
            accept.setOnClickListener(v2 -> {
                clickedOrder.accept(new Pair<>("send", arrivedOrders));
            });
        }
    }
}



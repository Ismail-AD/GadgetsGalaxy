package com.appdev.gadgetsgalaxy.recyclerview;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appdev.gadgetsgalaxy.R;
import com.appdev.gadgetsgalaxy.data.Admin_panel_data;
import com.appdev.gadgetsgalaxy.databinding.AdminElementLayoutBinding;

import java.util.List;
import java.util.function.Consumer;

public class Admin_card_adapter extends RecyclerView.Adapter<Admin_card_adapter.adminViewHolder> {
    int[] colorArray = {
            Color.parseColor("#FFFF5733"),   // Orange
            Color.parseColor("#FFC70039"),   // Red
            Color.parseColor("#FF900C3F"),   // Dark Pink
            Color.parseColor("#FF3F51B5"),   // Indigo
            Color.parseColor("#FF8BC34A"),   // Light Green
            Color.parseColor("#FF607D8B"),   // Blue Grey
            Color.parseColor("#FFFF9800"),   // Deep Orange
            Color.parseColor("#FF7B1FA2"),   // Purple
            Color.parseColor("#FF009688"),   // Teal
            Color.parseColor("#FF03A9F4"),   // Light Blue
            Color.parseColor("#FF795548")    // Brown
    };

    List<Admin_panel_data> adminPanelDataList;
    final Consumer<String> onAdminCardClickListener;

    public Admin_card_adapter(List<Admin_panel_data> DataList, Consumer<String> onAdminCardClickListener) {
        this.adminPanelDataList = DataList;
        this.onAdminCardClickListener = onAdminCardClickListener;
    }

    @NonNull
    @Override
    public adminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        AdminElementLayoutBinding adminElementLayoutBinding = AdminElementLayoutBinding.inflate(layoutInflater, parent, false);
        return new adminViewHolder(adminElementLayoutBinding, onAdminCardClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull adminViewHolder holder, int position) {
        Admin_panel_data panelData = adminPanelDataList.get(position);
        String title = panelData.getTitle();

        // Use a switch statement to handle different titles
        holder.adminElementLayoutBinding.setElementData(panelData);
        holder.adminElementLayoutBinding.executePendingBindings();
        holder.adminElementLayoutBinding.completeCard.startAnimation(AnimationUtils.loadAnimation(holder.adminElementLayoutBinding.getRoot().getContext(), R.anim.recycler_view_anim_single));
        holder.adminElementLayoutBinding.completeCard.setOnClickListener((view) -> {
            if (onAdminCardClickListener != null) {
                onAdminCardClickListener.accept(title);
            }
        });
        switch (title) {
            case "Customers":
                // Set vector drawable for Title 1
                holder.adminElementLayoutBinding.iconImage.setImageResource(R.drawable.customers);
                holder.adminElementLayoutBinding.iconBack.setBackgroundColor(colorArray[0]);
                break;
            case "Categories":
                // Set vector drawable for Title 2
                holder.adminElementLayoutBinding.iconImage.setImageResource(R.drawable.categories);
                holder.adminElementLayoutBinding.iconBack.setBackgroundColor(colorArray[1]);
                break;
            case "Products":
                // Set vector drawable for Title 2
                holder.adminElementLayoutBinding.iconImage.setImageResource(R.drawable.products);
                holder.adminElementLayoutBinding.iconBack.setBackgroundColor(colorArray[2]);
                break;
            case "Earnings":
                // Set vector drawable for Title 2
                holder.adminElementLayoutBinding.iconImage.setImageResource(R.drawable.earning);
                holder.adminElementLayoutBinding.iconBack.setBackgroundColor(colorArray[3]);
                break;
            case "Pending Orders":
                // Set vector drawable for Title 2
                holder.adminElementLayoutBinding.iconImage.setImageResource(R.drawable.pending);
                holder.adminElementLayoutBinding.iconBack.setBackgroundColor(colorArray[4]);
                break;

            case "Delivered":
                // Set vector drawable for Title 2
                holder.adminElementLayoutBinding.iconImage.setImageResource(R.drawable.delivered_orders);
                holder.adminElementLayoutBinding.iconBack.setBackgroundColor(colorArray[5]);
                break;
            case "Cancel Orders":
                // Set vector drawable for Title 2
                holder.adminElementLayoutBinding.iconImage.setImageResource(R.drawable.cancel);
                holder.adminElementLayoutBinding.iconBack.setBackgroundColor(colorArray[6]);
                break;
            case "Banners":
                // Set vector drawable for Title 2
                holder.adminElementLayoutBinding.iconImage.setImageResource(R.drawable.banners);
                holder.adminElementLayoutBinding.iconBack.setBackgroundColor(colorArray[7]);
                break;
            case "Discount":
                // Set vector drawable for Title 2
                holder.adminElementLayoutBinding.iconImage.setImageResource(R.drawable.discount);
                holder.adminElementLayoutBinding.iconBack.setBackgroundColor(colorArray[8]);
                break;

            default:
                // Set default vector drawable for other titles
                holder.adminElementLayoutBinding.iconImage.setImageResource(R.drawable.customers);
                holder.adminElementLayoutBinding.iconBack.setBackgroundColor(colorArray[5]);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return adminPanelDataList.size();
    }


    public static class adminViewHolder extends RecyclerView.ViewHolder {

        AdminElementLayoutBinding adminElementLayoutBinding;

        public adminViewHolder(@NonNull AdminElementLayoutBinding adminElementLayoutBinding, Consumer<String> onAdminCardClickListener) {
            super(adminElementLayoutBinding.getRoot());
            this.adminElementLayoutBinding = adminElementLayoutBinding;
        }
    }
}


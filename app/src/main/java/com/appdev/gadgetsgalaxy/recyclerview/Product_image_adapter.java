package com.appdev.gadgetsgalaxy.recyclerview;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appdev.gadgetsgalaxy.data.Category_info;
import com.appdev.gadgetsgalaxy.data.Product_info;
import com.appdev.gadgetsgalaxy.databinding.CategoryLayoutBinding;
import com.appdev.gadgetsgalaxy.databinding.ProductLayoutBinding;

import java.util.List;
import java.util.function.Consumer;

public class Product_image_adapter extends RecyclerView.Adapter<Product_image_adapter.itemInfoViewHolder> {


    List<Product_info> productInfoList;
    final Consumer<Pair<Product_info, ImageView>> passItemInfo;

    public Product_image_adapter(List<Product_info> DataList, Consumer<Pair<Product_info, ImageView>> passItemInfo) {
        this.productInfoList = DataList;
        this.passItemInfo = passItemInfo;
    }


    @NonNull
    @Override
    public itemInfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ProductLayoutBinding productLayoutBinding = ProductLayoutBinding.inflate(layoutInflater, parent, false);
        return new itemInfoViewHolder(productLayoutBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull itemInfoViewHolder holder, int position) {
        Product_info productInfo = productInfoList.get(position);

        // Use a switch statement to handle different titles
        holder.productLayoutBinding.setItemData(productInfo);
        holder.productLayoutBinding.executePendingBindings();
        holder.productLayoutBinding.completeCard.setOnClickListener(view -> {
            passItemInfo.accept(new Pair<>(productInfo, holder.productLayoutBinding.catImage));
        });

    }

    @Override
    public int getItemCount() {
        return productInfoList.size();
    }

    public static class itemInfoViewHolder extends RecyclerView.ViewHolder {

        ProductLayoutBinding productLayoutBinding;

        public itemInfoViewHolder(@NonNull ProductLayoutBinding productLayoutBinding) {
            super(productLayoutBinding.getRoot());
            this.productLayoutBinding = productLayoutBinding;

        }
    }
}


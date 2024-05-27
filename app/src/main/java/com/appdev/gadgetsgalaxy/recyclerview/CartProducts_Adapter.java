package com.appdev.gadgetsgalaxy.recyclerview;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.appdev.gadgetsgalaxy.R;
import com.appdev.gadgetsgalaxy.data.Product_info;
import com.appdev.gadgetsgalaxy.databinding.CartProductLayoutBinding;

import java.util.List;
import java.util.function.Consumer;

public class CartProducts_Adapter extends RecyclerView.Adapter<CartProducts_Adapter.itemInfoViewHolder> {


    List<Product_info> productInfoList;
    final Consumer<Pair<Product_info, Integer>> passItemInfo;

    public CartProducts_Adapter(List<Product_info> DataList, Consumer<Pair<Product_info, Integer>> passItemInfo) {
        this.productInfoList = DataList;
        this.passItemInfo = passItemInfo;
    }


    @NonNull
    @Override
    public itemInfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        CartProductLayoutBinding binding = CartProductLayoutBinding.inflate(layoutInflater, parent, false);
        return new itemInfoViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull itemInfoViewHolder holder, int position) {
        Product_info productInfo = productInfoList.get(position);

        holder.LayoutBinding.setItemData(productInfo);
        holder.LayoutBinding.executePendingBindings();
        if (productInfo.getItem_discounted_price() > 0) {
            holder.LayoutBinding.slash.setVisibility(View.VISIBLE);
            holder.LayoutBinding.disPrice.setVisibility(View.VISIBLE);
            holder.LayoutBinding.itemPrice.setTypeface(ResourcesCompat.getFont(holder.LayoutBinding.getRoot().getContext(), R.font.helvetica));
        } else {
            holder.LayoutBinding.slash.setVisibility(View.GONE);
            holder.LayoutBinding.disPrice.setVisibility(View.GONE);
            holder.LayoutBinding.itemPrice.setTypeface(ResourcesCompat.getFont(holder.LayoutBinding.getRoot().getContext(), R.font.helveticaneuemedium));
        }
        holder.LayoutBinding.plus.setOnClickListener(view -> {
            passItemInfo.accept(new Pair<>(productInfo, holder.LayoutBinding.plus.getId()));
        });
        holder.LayoutBinding.minus.setOnClickListener(view -> {
            if (productInfo.getSelectedQuantity() > 1) {
                passItemInfo.accept(new Pair<>(productInfo, holder.LayoutBinding.minus.getId()));
            }
        });
        holder.LayoutBinding.clearProduct.setOnClickListener(v -> {
            passItemInfo.accept(new Pair<>(productInfo, holder.LayoutBinding.clearProduct.getId()));
        });

    }

    @Override
    public int getItemCount() {
        return productInfoList.size();
    }

    public static class itemInfoViewHolder extends RecyclerView.ViewHolder {

        CartProductLayoutBinding LayoutBinding;

        public itemInfoViewHolder(@NonNull CartProductLayoutBinding LayoutBinding) {
            super(LayoutBinding.getRoot());
            this.LayoutBinding = LayoutBinding;

        }
    }
}


package com.appdev.gadgetsgalaxy.recyclerview;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.appdev.gadgetsgalaxy.data.Category_info;
import com.appdev.gadgetsgalaxy.databinding.CategoryLayoutBinding;

import java.util.List;
import java.util.function.Consumer;

public class Category_Image_Adapter extends RecyclerView.Adapter<Category_Image_Adapter.catInfoViewHolder> {


    List<Category_info> userInfoList;
    final Consumer<Pair<Category_info, ImageView>> passUserInfo;

    public Category_Image_Adapter(List<Category_info> DataList, Consumer<Pair<Category_info, ImageView>> passUserInfo) {
        this.userInfoList = DataList;
        this.passUserInfo = passUserInfo;
    }


    @NonNull
    @Override
    public catInfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        CategoryLayoutBinding categoryLayoutBinding = CategoryLayoutBinding.inflate(layoutInflater, parent, false);
        return new catInfoViewHolder(categoryLayoutBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull catInfoViewHolder holder, int position) {
        Category_info categoryInfo = userInfoList.get(position);

        // Use a switch statement to handle different titles
        holder.categoryLayoutBinding.setCategoryData(categoryInfo);
        holder.categoryLayoutBinding.executePendingBindings();
        holder.categoryLayoutBinding.catImage.setTransitionName(categoryInfo.getCatTitle());
        holder.categoryLayoutBinding.completeCard.setOnClickListener(view -> {
            passUserInfo.accept(new Pair<>(categoryInfo, holder.categoryLayoutBinding.catImage));
        });

    }

    @Override
    public int getItemCount() {
        return userInfoList.size();
    }


    public static class catInfoViewHolder extends RecyclerView.ViewHolder {

        CategoryLayoutBinding categoryLayoutBinding;

        public catInfoViewHolder(@NonNull CategoryLayoutBinding categoryLayoutBinding) {
            super(categoryLayoutBinding.getRoot());
            this.categoryLayoutBinding = categoryLayoutBinding;

        }
    }
}

package com.appdev.gadgetsgalaxy.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appdev.gadgetsgalaxy.data.User_info;
import com.appdev.gadgetsgalaxy.databinding.UserInfoLayoutBinding;

import java.util.List;
import java.util.function.Consumer;

public class Customer_info_adapter extends RecyclerView.Adapter<Customer_info_adapter.userInfoViewHolder> {


    List<User_info> userInfoList;
    final Consumer<User_info> passUserInfo;

    public Customer_info_adapter(List<User_info> DataList, Consumer<User_info> passUserInfo) {
        this.userInfoList = DataList;
        this.passUserInfo = passUserInfo;
    }


    @NonNull
    @Override
    public userInfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        UserInfoLayoutBinding userInfoLayoutBinding = UserInfoLayoutBinding.inflate(layoutInflater, parent, false);
        return new userInfoViewHolder(userInfoLayoutBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull userInfoViewHolder holder, int position) {
        User_info userInfo = userInfoList.get(position);

        // Use a switch statement to handle different titles
        holder.userInfoLayoutBinding.setUserData(userInfo);
        holder.userInfoLayoutBinding.executePendingBindings();
        holder.userInfoLayoutBinding.completeCard.setOnClickListener(view -> {
            passUserInfo.accept(userInfo);
        });

    }

    @Override
    public int getItemCount() {
        return userInfoList.size();
    }


    public static class userInfoViewHolder extends RecyclerView.ViewHolder {

        UserInfoLayoutBinding userInfoLayoutBinding;

        public userInfoViewHolder(@NonNull UserInfoLayoutBinding infoLayoutBinding) {
            super(infoLayoutBinding.getRoot());
            this.userInfoLayoutBinding = infoLayoutBinding;

        }
    }
}

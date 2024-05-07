package com.appdev.gadgetsgalaxy;

import static androidx.navigation.fragment.FragmentKt.findNavController;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appdev.gadgetsgalaxy.data.User_info;
import com.appdev.gadgetsgalaxy.databinding.FragmentUserInfoScreenBinding;
import com.appdev.gadgetsgalaxy.databinding.UserInfoDialogBinding;
import com.appdev.gadgetsgalaxy.recyclerview.Customer_info_adapter;

import java.util.ArrayList;
import java.util.List;


public class userInfo_screen extends Fragment {

    private FragmentUserInfoScreenBinding screenBinding;
    Customer_info_adapter customerInfoAdapter;
    private UserInfoDialogBinding dialogBinding;
    List<User_info> userInfoList = new ArrayList<>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        screenBinding = FragmentUserInfoScreenBinding.inflate(inflater,container,false);
        userInfoList.add(new User_info(0,0,"1","John kelvin","okjohnk@gmail.com","+01921929","nobody address",""));
        userInfoList.add(new User_info(0,0,"1","John kelvin","okjohnk@gmail.com","+01921929","nobody address",""));
        userInfoList.add(new User_info(0,0,"1","John kelvin","okjohnk@gmail.com","+01921929","nobody address",""));
        userInfoList.add(new User_info(0,0,"1","John kelvin","okjohnk@gmail.com","+01921929","nobody address",""));
        userInfoList.add(new User_info(0,0,"1","John kelvin","okjohnk@gmail.com","+01921929","nobody address",""));
        userInfoList.add(new User_info(0,0,"1","John kelvin","okjohnk@gmail.com","+01921929","nobody address",""));
        userInfoList.add(new User_info(0,0,"1","John kelvin","okjohnk@gmail.com","+01921929","nobody address",""));
        customerInfoAdapter = new Customer_info_adapter(userInfoList,this::showDialogWithInfo);
        screenBinding.rv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        screenBinding.rv.setAdapter(customerInfoAdapter);
        screenBinding.backBtn.setOnClickListener(view -> {
            findNavController(this).popBackStack();
        });
        return screenBinding.getRoot();
    }
    public void showDialogWithInfo(User_info userInfo){
        dialogBinding = UserInfoDialogBinding.inflate(getLayoutInflater());
        dialogBinding.setUserData(userInfo);
        dialogBinding.executePendingBindings();
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext(),R.style.CurvedCornersDialog);
        builder.setView(dialogBinding.getRoot());
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.setCancelable(false);
        dialogBinding.closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              alertDialog.dismiss();
            }
        });
    }
}
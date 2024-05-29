package com.appdev.gadgetsgalaxy;

import static androidx.navigation.fragment.FragmentKt.findNavController;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.appdev.gadgetsgalaxy.data.User_info;
import com.appdev.gadgetsgalaxy.databinding.FragmentUserInfoScreenBinding;
import com.appdev.gadgetsgalaxy.databinding.UserInfoDialogBinding;
import com.appdev.gadgetsgalaxy.recyclerview.Customer_info_adapter;
import com.appdev.gadgetsgalaxy.utils.FirebaseUtil;
import com.appdev.gadgetsgalaxy.utils.Utility;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class userInfo_screen extends Fragment {

    private FragmentUserInfoScreenBinding screenBinding;
    Customer_info_adapter customerInfoAdapter;
    private UserInfoDialogBinding dialogBinding;
    List<User_info> userInfoList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Utility.isDarkModeActivated(requireActivity())) {
            Utility.status_bar_dark(requireActivity(), R.color.black);
        } else {
            Utility.status_bar(requireActivity(), R.color.white);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        screenBinding = FragmentUserInfoScreenBinding.inflate(inflater, container, false);


        customerInfoAdapter = new Customer_info_adapter(userInfoList, this::showDialogWithInfo);
        screenBinding.rv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        screenBinding.rv.setAdapter(customerInfoAdapter);
        getAllUsers();
        screenBinding.backBtn.setOnClickListener(view -> {
            findNavController(this).popBackStack();
        });
        return screenBinding.getRoot();
    }

    private void getAllUsers() {
        FirebaseUtil.getFirebaseDatabase().getReference().child("userProfiles").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userInfoList.clear();
                for(DataSnapshot data:snapshot.getChildren()){
                    User_info userInfo = data.getValue(User_info.class);
                    if(!Objects.equals(userInfo != null ? userInfo.getUserType() : null, "ADMIN")){
                        userInfoList.add(userInfo);
                    }
                }
                customerInfoAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void showDialogWithInfo(User_info userInfo) {
        dialogBinding = UserInfoDialogBinding.inflate(getLayoutInflater());
        dialogBinding.setUserData(userInfo);
        dialogBinding.executePendingBindings();
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext(), R.style.CurvedCornersDialog);
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
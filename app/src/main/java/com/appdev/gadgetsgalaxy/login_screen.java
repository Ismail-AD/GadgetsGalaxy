package com.appdev.gadgetsgalaxy;

import static androidx.navigation.fragment.FragmentKt.findNavController;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appdev.gadgetsgalaxy.databinding.FragmentLoginScreenBinding;

public class login_screen extends Fragment {

    private FragmentLoginScreenBinding loginScreenBinding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        loginScreenBinding = FragmentLoginScreenBinding.inflate(inflater,container,false);
        loginScreenBinding.loginBtn.setOnClickListener(view -> {
            findNavController(this).navigate(R.id.action_login_screen_to_admin_page);
        });
        return loginScreenBinding.getRoot();
    }
}
package com.appdev.gadgetsgalaxy;

import static androidx.navigation.fragment.FragmentKt.findNavController;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appdev.gadgetsgalaxy.databinding.FragmentWelcomeScreenBinding;


public class welcome_screen extends Fragment {

    private FragmentWelcomeScreenBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWelcomeScreenBinding.inflate(inflater, container, false);
        binding.regBtn.setOnClickListener(view -> {
            findNavController(this).navigate(R.id.action_welcome_screen_to_signup_screen);
        });
        binding.loginBtn.setOnClickListener(view -> {
            findNavController(this).navigate(R.id.action_welcome_screen_to_login_screen);
        });
        return binding.getRoot();
    }
}
package com.appdev.gadgetsgalaxy;

import static androidx.navigation.fragment.FragmentKt.findNavController;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appdev.gadgetsgalaxy.databinding.FragmentSignupScreenBinding;

public class signup_screen extends Fragment {

    FragmentSignupScreenBinding screenBinding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        screenBinding = FragmentSignupScreenBinding.inflate(inflater,container,false);
        screenBinding.registerBtn.setOnClickListener(v->{

        });

        return screenBinding.getRoot();
    }
}
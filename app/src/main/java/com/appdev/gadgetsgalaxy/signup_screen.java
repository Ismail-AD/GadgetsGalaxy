package com.appdev.gadgetsgalaxy;

import static androidx.navigation.fragment.FragmentKt.findNavController;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.appdev.gadgetsgalaxy.data.User_info;
import com.appdev.gadgetsgalaxy.databinding.FragmentSignupScreenBinding;
import com.appdev.gadgetsgalaxy.utils.Validation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class signup_screen extends Fragment {

    FragmentSignupScreenBinding screenBinding;
    private FirebaseAuth firebaseAuth;
    private final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        screenBinding = FragmentSignupScreenBinding.inflate(inflater, container, false);

        firebaseAuth = FirebaseAuth.getInstance();

        screenBinding.registerBtn.setOnClickListener(v -> {


            String email = screenBinding.suMail.getText().toString();
            String userName = screenBinding.suUser.getText().toString();
            String password = screenBinding.suPass.getText().toString();
            String confirmPassword = screenBinding.suCpass.getText().toString();

            String checkMessage = new Validation().newUserValidationCheck(email, password, confirmPassword);
            boolean userValidationReceived = new Validation().newUserConfirmValidationCheck(email, password, confirmPassword);

            if (userValidationReceived) {

                screenBinding.pg.setVisibility(View.VISIBLE);
                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        User_info userProfile = new User_info("", firebaseAuth.getUid(), userName, email, "", "", "", "USER");
                        firebaseDatabase.getReference().child("userProfiles")
                                .child(Objects.requireNonNull(firebaseAuth.getUid()))
                                .setValue(userProfile)
                                .addOnSuccessListener(aVoid -> {
                                    findNavController(this).popBackStack();
                                });
                    } else {
                        screenBinding.pg.setVisibility(View.GONE);
                        Toast.makeText(requireContext(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                screenBinding.pg.setVisibility(View.GONE);
                Toast.makeText(requireContext(), checkMessage, Toast.LENGTH_SHORT).show();
            }

        });

        return screenBinding.getRoot();
    }
}
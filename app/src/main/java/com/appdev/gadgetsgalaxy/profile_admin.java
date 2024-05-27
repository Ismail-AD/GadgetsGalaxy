package com.appdev.gadgetsgalaxy;

import static androidx.navigation.fragment.FragmentKt.findNavController;

import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.appdev.gadgetsgalaxy.data.User_info;
import com.appdev.gadgetsgalaxy.databinding.FragmentProfileAdminBinding;
import com.appdev.gadgetsgalaxy.databinding.ProgressBarBinding;
import com.appdev.gadgetsgalaxy.utils.FirebaseUtil;
import com.bumptech.glide.Glide;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class profile_admin extends Fragment {

    FragmentProfileAdminBinding binding;
    User_info user;
    private ActivityResultLauncher<String> getContentLauncher;
    ProgressBarBinding progressBarBinding;
    Dialog progressDialog;

    Uri imageUri;

    int tasksCompleted = 0; // To keep track of completed tasks
    int totalTasks = 0;
    Map<String, Object> updates = new HashMap<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileAdminBinding.inflate(inflater, container, false);
        progressBarBinding = ProgressBarBinding.inflate(inflater);
        progressDialog = new Dialog(requireContext());

        user = profile_adminArgs.fromBundle(getArguments()).getUser();
        imageUri = Uri.parse(user.getImageUrl());
        setUpViews(user);


        getContentLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        imageUri = uri;
                        Glide.with(requireContext()).load(uri).into(binding.pimage);
                    } else {
                        imageUri = null;
                        Toast.makeText(requireContext(), "No image selected", Toast.LENGTH_SHORT).show();
                    }
                });
        binding.pimage.setOnClickListener(v -> {
            getContentLauncher.launch("image/*");
        });

        return binding.getRoot();
    }

    private void setUpViews(User_info user) {
        Glide.with(requireContext()).load(user.getImageUrl()).placeholder(R.drawable.profileimageph).into(binding.pimage);
        binding.suMail.setText(user.getEmail());
        binding.suUser.setText(user.getName());
        binding.name.setText(user.getName());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        binding.savecatbtn.setOnClickListener(v -> {
            showProgressDialog();



            if (!binding.suUser.getText().toString().isEmpty()) {
                if (binding.suUser.getText().toString().equals(user.getName()) && imageUri.toString().equals(user.getImageUrl())) {
                    dismissProgressDialog();
                    Toast.makeText(requireContext(), "No changes have been made yet !", Toast.LENGTH_SHORT).show();
                } else {
                    totalTasks = ((!imageUri.toString().equals(user.getImageUrl()) ? 1 : 0) + (!binding.suUser.getText().toString().equals(user.getName()) ? 1 : 0));
                    updateUserInDatabase(binding.suUser.getText().toString(), imageUri, !imageUri.toString().equals(user.getImageUrl()), !binding.suUser.getText().toString().equals(user.getName()));
                }
            } else {
                dismissProgressDialog();
                Toast.makeText(requireContext(), "Fill out missing fields !", Toast.LENGTH_SHORT).show();
            }


        });

        binding.backBtn.setOnClickListener(view2 -> {
            findNavController(this).popBackStack();
        });
    }

    private void updateUserInDatabase(String newUsername, Uri newImageUri, boolean imageChanged, boolean usernameChanged) {


        if (usernameChanged) {
            updates.put("name", newUsername);
            checkAllTasksCompleted(updates);
        }
        if (imageChanged) {
            StorageReference imageRef = FirebaseUtil.getStorageReference().child(System.currentTimeMillis() + ".jpg");
            imageRef.putFile(newImageUri)
                    .addOnSuccessListener(taskSnapshot -> imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String downloadUrl = uri.toString();
                        updates.put("imageUrl", downloadUrl);
                        checkAllTasksCompleted(updates);
                    }))
                    .addOnFailureListener(e -> {
                        Toast.makeText(requireContext(), "Failed to upload image to storage", Toast.LENGTH_SHORT).show();
                    });

        }
    }

    private void checkAllTasksCompleted(Map<String, Object> updates) {
        tasksCompleted++;
        if (tasksCompleted == totalTasks) {
            FirebaseUtil.getFirebaseDatabase().getReference().child("userProfiles")
                    .child(Objects.requireNonNull(FirebaseUtil.getFirebaseAuth().getUid())).updateChildren(updates).addOnCompleteListener(task -> {
                        dismissProgressDialog();
                        if (task.isSuccessful()) {
                            Toast.makeText(requireContext(), "Profile updated successfully!", Toast.LENGTH_SHORT).show();
                            findNavController(this).popBackStack();
                        } else {
                            Toast.makeText(requireContext(), "Profile update failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    public void showProgressDialog() {
        progressDialog.setContentView(R.layout.progress_bar);
        Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
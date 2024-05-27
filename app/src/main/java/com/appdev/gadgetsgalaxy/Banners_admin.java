package com.appdev.gadgetsgalaxy;

import static androidx.navigation.fragment.FragmentKt.findNavController;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.appdev.gadgetsgalaxy.databinding.BannerCreationBinding;
import com.appdev.gadgetsgalaxy.databinding.DeleteimagesLayoutBinding;
import com.appdev.gadgetsgalaxy.databinding.FragmentBannersAdminBinding;
import com.appdev.gadgetsgalaxy.recyclerview.Banner_image_adapter;
import com.appdev.gadgetsgalaxy.utils.FirebaseUtil;
import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.constants.AnimationTypes;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Objects;


public class Banners_admin extends Fragment {
    FragmentBannersAdminBinding binding;
    BannerCreationBinding creationBinding;
    DeleteimagesLayoutBinding deleteimagesLayoutBinding;
    BottomSheetDialog bottomSheetDialog;
    private ActivityResultLauncher<String> getContentLauncher;
    Uri imageUri;
    RadioButton selectedRadioButton;
    ArrayList<String> homeList = new ArrayList<>();

    ArrayList<String> catList = new ArrayList<>();
    ArrayList<String> elementsToRemove = new ArrayList<>();

    ValueEventListener valueEventListenerForHome;
    ValueEventListener valueEventListenerForCat;
    ArrayList<SlideModel> imageListHome = new ArrayList<>();
    ArrayList<SlideModel> imageListCat = new ArrayList<>();
    Banner_image_adapter bannerImageAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBannersAdminBinding.inflate(inflater, container, false);

        binding.imageSliderHome.setSlideAnimation(AnimationTypes.ZOOM_IN);
        binding.imageSliderCat.setSlideAnimation(AnimationTypes.ZOOM_IN);

        getContentLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        imageUri = uri;
                        Glide.with(requireContext()).load(uri).into(creationBinding.image);
                        ViewGroup.LayoutParams params = creationBinding.image.getLayoutParams();
                        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
                        creationBinding.image.setLayoutParams(params);
                    } else {
                        Toast.makeText(requireContext(), "No image selected", Toast.LENGTH_SHORT).show();
                    }
                });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.delBtn.setOnClickListener(v -> {
            bottomSheetDialog = new BottomSheetDialog(
                    requireContext());
            deleteimagesLayoutBinding = DeleteimagesLayoutBinding.inflate(getLayoutInflater());
            deleteimagesLayoutBinding.closeBottomSheet.setOnClickListener(view1 -> bottomSheetDialog.dismiss());
            bottomSheetDialog.setContentView(deleteimagesLayoutBinding.getRoot());

            bannerImageAdapter = new Banner_image_adapter(homeList, this::onBannerDeleteClick);
            deleteimagesLayoutBinding.rv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            deleteimagesLayoutBinding.rv.setAdapter(bannerImageAdapter);

            deleteimagesLayoutBinding.savecatbtn.setOnClickListener(vhome -> {
                deleteimagesLayoutBinding.pg.setVisibility(View.VISIBLE);
                deleteimagesLayoutBinding.savecatbtn.setVisibility(View.INVISIBLE);
                updateList("Home");
            });

            Objects.requireNonNull(bottomSheetDialog.getWindow()).setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            bottomSheetDialog.setCancelable(false);
            bottomSheetDialog.show();
        });

        binding.delBtnCat.setOnClickListener(vcat -> {
            bottomSheetDialog = new BottomSheetDialog(
                    requireContext());
            deleteimagesLayoutBinding = DeleteimagesLayoutBinding.inflate(getLayoutInflater());
            deleteimagesLayoutBinding.closeBottomSheet.setOnClickListener(view1 -> bottomSheetDialog.dismiss());
            bottomSheetDialog.setContentView(deleteimagesLayoutBinding.getRoot());

            bannerImageAdapter = new Banner_image_adapter(catList, this::onBannerDeleteClick);
            deleteimagesLayoutBinding.rv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            deleteimagesLayoutBinding.rv.setAdapter(bannerImageAdapter);

            deleteimagesLayoutBinding.savecatbtn.setOnClickListener(v -> {
                deleteimagesLayoutBinding.pg.setVisibility(View.VISIBLE);
                deleteimagesLayoutBinding.savecatbtn.setVisibility(View.INVISIBLE);
                updateList("Category");
            });

            Objects.requireNonNull(bottomSheetDialog.getWindow()).setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            bottomSheetDialog.setCancelable(false);
            bottomSheetDialog.show();
        });


        binding.backBtn.setOnClickListener(view1 -> {
            findNavController(this).popBackStack();
        });

        binding.floatingButton.setOnClickListener(v -> {
            bottomSheetDialog = new BottomSheetDialog(
                    requireContext());
            creationBinding = BannerCreationBinding.inflate(getLayoutInflater());
            creationBinding.closeBottomSheet.setOnClickListener(view1 -> bottomSheetDialog.dismiss());
            bottomSheetDialog.setContentView(creationBinding.getRoot());

            creationBinding.image.setOnClickListener(view12 -> getContentLauncher.launch("image/*"));

            selectedRadioButton = creationBinding.getRoot().findViewById(R.id.radio0);
            creationBinding.dailyWeeklyButtonView.setOnCheckedChangeListener((group, checkedId) -> {
                int radioButtonId = group.getCheckedRadioButtonId();
                selectedRadioButton = creationBinding.getRoot().findViewById(radioButtonId);
            });

            creationBinding.addBannerBtn.setOnClickListener(imgview -> {
                creationBinding.pg.setVisibility(View.VISIBLE);
                creationBinding.addBannerBtn.setVisibility(View.INVISIBLE);
                DatabaseReference databaseReference = FirebaseUtil.getFirebaseDatabase().getReference().child("Banners").child(selectedRadioButton.getText().toString());
                String Id = databaseReference.push().getKey();
                StorageReference imageRef = FirebaseUtil.getStorageReference().child(System.currentTimeMillis() + ".jpg");
                imageRef.putFile(imageUri)
                        .addOnSuccessListener(taskSnapshot -> {
                            imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                String downloadUrl = uri.toString();
                                databaseReference.child(Id).setValue(downloadUrl)
                                        .addOnSuccessListener(aVoid -> {
                                            Toast.makeText(requireContext(), "Banner created successfully", Toast.LENGTH_SHORT).show();
                                            creationBinding.pg.setVisibility(View.INVISIBLE);
                                            creationBinding.addBannerBtn.setVisibility(View.VISIBLE);
                                            bottomSheetDialog.dismiss();
                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(requireContext(), "Failed to create Banner", Toast.LENGTH_SHORT).show();
                                            creationBinding.pg.setVisibility(View.INVISIBLE);
                                            creationBinding.addBannerBtn.setVisibility(View.VISIBLE);
                                        });
                            });
                        }).addOnFailureListener(e -> {
                            creationBinding.pg.setVisibility(View.INVISIBLE);
                            creationBinding.addBannerBtn.setVisibility(View.VISIBLE);
                            Toast.makeText(requireContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        });
            });

            Objects.requireNonNull(bottomSheetDialog.getWindow()).setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            bottomSheetDialog.setCancelable(false);
            bottomSheetDialog.show();

        });


        ValueEventListener categoryListenerHome = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                homeList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String fetchedUrl = dataSnapshot.getValue(String.class);
                    homeList.add(fetchedUrl);
                }
                setUpViewsForHome();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };
        this.valueEventListenerForHome = categoryListenerHome;
        FirebaseUtil.getFirebaseDatabase().getReference()
                .child("Banners").child("Home")
                .addValueEventListener(valueEventListenerForHome);

        // Store the listener in a member variable to remove it later


        ValueEventListener categoryListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                catList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String fetchedUrl = dataSnapshot.getValue(String.class);
                    catList.add(fetchedUrl);
                }
                setUpViewsForCat();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };
        this.valueEventListenerForCat = categoryListener;
        FirebaseUtil.getFirebaseDatabase().getReference()
                .child("Banners").child("Category")
                .addValueEventListener(valueEventListenerForCat);

    }

    private void updateList(String parent) {
        DatabaseReference bannerRef = FirebaseUtil.getFirebaseDatabase().getReference().child("Banners").child(parent);
        bannerRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        String url = childSnapshot.getValue(String.class);
                        if (elementsToRemove.contains(url)) {
                            childSnapshot.getRef().removeValue().addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    deleteimagesLayoutBinding.pg.setVisibility(View.INVISIBLE);
                                    deleteimagesLayoutBinding.savecatbtn.setVisibility(View.VISIBLE);
                                    bottomSheetDialog.dismiss();
                                    Toast.makeText(requireContext(), "Banner removed successfully", Toast.LENGTH_SHORT).show();
                                    elementsToRemove.clear();
                                } else {
                                    Toast.makeText(requireContext(), "Failed to remove banner", Toast.LENGTH_SHORT).show();
                                    deleteimagesLayoutBinding.pg.setVisibility(View.INVISIBLE);
                                    deleteimagesLayoutBinding.savecatbtn.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(), "Failed to retrieve banners", Toast.LENGTH_SHORT).show();
                deleteimagesLayoutBinding.pg.setVisibility(View.INVISIBLE);
                deleteimagesLayoutBinding.savecatbtn.setVisibility(View.VISIBLE);
            }
        });
    }

    private void onBannerDeleteClick(String s) {
        elementsToRemove.add(s);
        if (homeList.contains(s)) {
            homeList.remove(s);
        } else if (catList.contains(s)) {
            catList.remove(s);
        } else {

        }
        bannerImageAdapter.notifyDataSetChanged();
    }

    public void hideHomeCard() {
        binding.homeCard.setVisibility(View.GONE);
        binding.pgHome.setVisibility(View.GONE);
        binding.homemsg.setVisibility(View.VISIBLE);
    }

    public void hideCatCard() {
        binding.CatCard.setVisibility(View.GONE);
        binding.pgCat.setVisibility(View.GONE);
        binding.Catmsg.setVisibility(View.VISIBLE);
    }

    public void showHomeCard() {
        binding.homeCard.setVisibility(View.VISIBLE);
        binding.pgHome.setVisibility(View.GONE);
        binding.homemsg.setVisibility(View.GONE);
    }

    public void showCatCard() {
        binding.CatCard.setVisibility(View.VISIBLE);
        binding.pgCat.setVisibility(View.GONE);
        binding.Catmsg.setVisibility(View.GONE);
    }

    private void setUpViewsForHome() {
        imageListHome.clear();
        for (String url : homeList) {
            imageListHome.add(new SlideModel(url, ScaleTypes.FIT));
        }
        binding.imageSliderHome.setImageList(imageListHome);
        if (homeList.isEmpty()) {
            hideHomeCard();
        } else {
            showHomeCard();
        }
    }

    private void setUpViewsForCat() {
        imageListCat.clear();
        for (String url : catList) {
            imageListCat.add(new SlideModel(url, ScaleTypes.FIT));
        }
        binding.imageSliderCat.setImageList(imageListCat);
        if (catList.isEmpty()) {
            hideCatCard();
        } else {
            showCatCard();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (valueEventListenerForHome != null) {
            FirebaseUtil.getFirebaseDatabase().getReference()
                    .child("Banners").child("Home")
                    .removeEventListener(valueEventListenerForHome);
        }
        if (valueEventListenerForCat != null) {
            FirebaseUtil.getFirebaseDatabase().getReference()
                    .child("Banners").child("Category")
                    .removeEventListener(valueEventListenerForCat);
        }
    }
}
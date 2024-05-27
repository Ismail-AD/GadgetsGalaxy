package com.appdev.gadgetsgalaxy;

import static androidx.navigation.fragment.FragmentKt.findNavController;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.NavDirections;
import androidx.navigation.NavOptions;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.appdev.gadgetsgalaxy.data.Admin_panel_data;
import com.appdev.gadgetsgalaxy.data.User_info;
import com.appdev.gadgetsgalaxy.databinding.FragmentAdminPageBinding;
import com.appdev.gadgetsgalaxy.recyclerview.Admin_card_adapter;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class admin_page extends Fragment {

    private FragmentAdminPageBinding adminPageBinding;
    Admin_card_adapter adminCardAdapter;
    List<Admin_panel_data> panelDataList = new ArrayList<>();
    private static final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private static final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private static final String PREFS_NAME = "TypePrefs";
    private static final String KEY_USER_TYPE = "userType";

    long userCount;
    User_info userProfile;
    LiveData<Integer> userCountLiveData = getCurrentUsersCount();
//    LiveData<Integer> categoryCountLiveData = getCategoryCount();
//    LiveData<Integer> productCountLiveData = getProductCount();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        adminPageBinding = FragmentAdminPageBinding.inflate(inflater, container, false);


        panelDataList.clear();

        userCountLiveData.observe(getViewLifecycleOwner(), count -> {
            panelDataList.add(new Admin_panel_data(count, "Customers"));
        });
        panelDataList.add(new Admin_panel_data(267, "Categories"));
        panelDataList.add(new Admin_panel_data(267, "Products"));
        panelDataList.add(new Admin_panel_data(267, "Earnings"));
        panelDataList.add(new Admin_panel_data(267, "Pending Orders"));
        panelDataList.add(new Admin_panel_data(267, "Delivered Orders"));
        panelDataList.add(new Admin_panel_data(267, "Cancel Orders"));
        panelDataList.add(new Admin_panel_data(267, "Banners"));
        adminCardAdapter = new Admin_card_adapter(panelDataList, this::onAdminCardClick);
        adminPageBinding.rv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        adminPageBinding.rv.setAdapter(adminCardAdapter);
        return adminPageBinding.getRoot();
    }


    public LiveData<Integer> getCurrentUsersCount() {
        MutableLiveData<Integer> userCountLiveData = new MutableLiveData<>();
        firebaseDatabase.getReference().child("userProfiles")
                .orderByChild("userType").equalTo("USER")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        userCountLiveData.setValue((int) dataSnapshot.getChildrenCount());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        userCountLiveData.setValue(0);
                    }
                });
        return userCountLiveData;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseDatabase.getReference().child("userProfiles").child(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid())
                .get().addOnCompleteListener(profileTask -> {
                    if (profileTask.isSuccessful() && profileTask.getResult().exists()) {
                        userProfile = profileTask.getResult().getValue(User_info.class);

                        if (userProfile != null) {
                            adminPageBinding.pg.setVisibility(View.GONE);
                            if (userProfile.getName().length() > 8) {
                                adminPageBinding.adminName.setText(userProfile.getName().substring(0, 8) + "..");
                            } else {
                                adminPageBinding.adminName.setText(userProfile.getName());
                            }
                            adminPageBinding.adminName.setText(userProfile.getName());
                            Glide.with(requireContext()).load(userProfile.getImageUrl()).placeholder(R.drawable.profileimageph).into(adminPageBinding.profile);
                        }
                    }
                });


        adminPageBinding.profilecard.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(requireContext(), v, 0, 0, R.style.PopupTheme);
            popupMenu.setOnMenuItemClickListener(menuItem -> {

                String title = Objects.requireNonNull(menuItem.getTitle()).toString();
                switch (title) {
                    case "Account":
                        NavDirections action = admin_pageDirections.actionAdminPageToProfileAdmin(userProfile);
                        findNavController(this).navigate(action);
                        return true;
                    case "Logout":
                        firebaseAuth.signOut();
                        SharedPreferences.Editor editor = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
                        editor.clear().apply();
                        NavOptions navOptions = new NavOptions.Builder()
                                .setPopUpTo(R.id.admin_page, true)
                                .build();
                        findNavController(this).navigate(R.id.login_screen, null, navOptions);
                        return true;
                    default:
                        // Default case
                        return false;
                }
            });
            popupMenu.inflate(R.menu.admin_menu);
            popupMenu.show();
        });
    };

    public void onAdminCardClick(String title) {
        switch (title) {
            case "Customers":
                findNavController(this).navigate(R.id.action_admin_page_to_userInfo_screen);
                break;
            case "Categories":
                findNavController(this).navigate(R.id.action_admin_page_to_categoryInfo);
                break;
            case "Products":
                findNavController(this).navigate(R.id.action_admin_page_to_product_showcase);
                break;
            case "Earnings":

                break;
            case "Pending Orders":
                findNavController(this).navigate(R.id.action_admin_page_to_pendingOrders);
                break;

            case "Delivered":

                break;
            case "Cancel Orders":

                break;
            case "Banners":
                findNavController(this).navigate(R.id.action_admin_page_to_banners_admin);
                break;

            default:
                Toast.makeText(getContext(), "Event Listener Not Implemented for following !", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
package com.appdev.gadgetsgalaxy;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.appdev.gadgetsgalaxy.databinding.ActivityMainBinding;
import com.appdev.gadgetsgalaxy.utils.FirebaseUtil;
import com.appdev.gadgetsgalaxy.utils.Utility;
import com.google.android.material.badge.BadgeDrawable;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    boolean keepIt = true;
    final int keepFor = 1000;

    private ActivityMainBinding binding;
    private static final String PREFS_NAME = "MyPrefs";
    private static final String KEY_FIRST_ENTRY_TIME = "firstEntryTime";

    private NavController navController;
    private static final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public ValueEventListener eventListenerForWishlist;
    public ValueEventListener eventListenerForCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);

        splashScreen.setKeepOnScreenCondition(() -> keepIt);


        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        long firstEntryTime = sharedPreferences.getLong(KEY_FIRST_ENTRY_TIME, -1);


        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(binding.navHostFragment.getId());
        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();

            // Set up BottomNavigationView with NavController
            NavigationUI.setupWithNavController(binding.bottomNavigationView, navController);


            // Add a destination listener to control the BottomNavigationView visibility
            navController.addOnDestinationChangedListener(
                    (controller, destination, arguments) -> {
                        if (destination.getId() == R.id.home_page || destination.getId() == R.id.myWishList ||
                                destination.getId() == R.id.see_all_categories || destination.getId() == R.id.CartScreen || destination.getId() == R.id.account_settings) {
                            binding.bottomNavigationView.setVisibility(View.VISIBLE);
                        } else {
                            binding.bottomNavigationView.setVisibility(View.GONE);
                        }
                    });

        }


        if (firstEntryTime == -1) {
            long currentTimeMillis = System.currentTimeMillis();
            sharedPreferences.edit().putLong(KEY_FIRST_ENTRY_TIME, currentTimeMillis).apply();
            navController.navigate(R.id.welcome_screen);
            keepIt = false;
        } else {
            // Not the first time entering the app, perform other action
            moveToLoginFragment();
        }


    }


    private void moveToLoginFragment() {
        NavOptions navOptions = new NavOptions.Builder()
                .setPopUpTo(R.id.welcome_screen, true)
                .build();
        if (firebaseAuth.getCurrentUser() != null) {
            String type = Utility.getUserTypeFromPrefs(this);
            if (!type.trim().isEmpty()) {
                if ("ADMIN".equals(type)) {
                    navController.navigate(R.id.admin_page, null, navOptions);
                } else if ("USER".equals(type)) {
                    navController.navigate(R.id.home_page, null, navOptions);
                }
            } else {
                navController.navigate(R.id.login_screen, null, navOptions);
            }
        } else {
            navController.navigate(R.id.login_screen, null, navOptions);
        }
        keepIt = false;
    }

    public void hideBottomNavigationView() {
        if (binding.bottomNavigationView.getVisibility() == View.VISIBLE) {
            binding.bottomNavigationView.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        BadgeDrawable drawable = binding.bottomNavigationView.getOrCreateBadge(R.id.myWishList);
        BadgeDrawable drawablec = binding.bottomNavigationView.getOrCreateBadge(R.id.CartScreen);
        drawable.setBackgroundColor(getResources().getColor(R.color.Orange, null));
        drawable.setBadgeTextColor(Color.WHITE);
        drawable.setVerticalOffset(6);
        drawable.setHorizontalOffset(1);
        drawable.setVisible(true);

        drawablec.setBackgroundColor(getResources().getColor(R.color.Orange, null));
        drawablec.setBadgeTextColor(Color.WHITE);
        drawablec.setVerticalOffset(6);
        drawablec.setHorizontalOffset(1);
        drawablec.setVisible(true);
        eventListenerForWishlist = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long count = snapshot.getChildrenCount();
                if ((int) count <= 0) {
                    drawable.setVisible(false);
                } else {
                    if(!drawable.isVisible()){
                        drawable.setVisible(true);
                    }
                    drawable.setNumber((int) count);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        eventListenerForCart = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long count = snapshot.getChildrenCount();
                if ((int) count <= 0) {
                    drawablec.setVisible(false);
                } else {
                    if(!drawablec.isVisible()){
                        drawablec.setVisible(true);
                    }
                    drawablec.setNumber((int) count);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        if (FirebaseUtil.getFirebaseAuth().getUid() != null) {
            FirebaseUtil.getFirebaseDatabase()
                    .getReference()
                    .child("Wishlist")
                    .child(FirebaseUtil.getFirebaseAuth().getUid()).addValueEventListener(eventListenerForWishlist);
            FirebaseUtil.getFirebaseDatabase()
                    .getReference()
                    .child("Cart")
                    .child(FirebaseUtil.getFirebaseAuth().getUid()).addValueEventListener(eventListenerForCart);
        }
    }

    public void checkAvailability() {
        if (FirebaseUtil.getFirebaseAuth().getUid() != null && eventListenerForCart != null && eventListenerForWishlist != null) {
            FirebaseUtil.getFirebaseDatabase()
                    .getReference()
                    .child("Wishlist")
                    .child(FirebaseUtil.getFirebaseAuth().getUid()).addValueEventListener(eventListenerForWishlist);
            FirebaseUtil.getFirebaseDatabase()
                    .getReference()
                    .child("Cart")
                    .child(FirebaseUtil.getFirebaseAuth().getUid()).addValueEventListener(eventListenerForCart);
        }
    }
}
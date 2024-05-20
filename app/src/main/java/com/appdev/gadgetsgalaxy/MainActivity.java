package com.appdev.gadgetsgalaxy;

import static androidx.navigation.fragment.FragmentKt.findNavController;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import androidx.fragment.app.FragmentContainerView;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.appdev.gadgetsgalaxy.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    boolean keepIt = true;
    final int keepFor = 1000;

    private ActivityMainBinding binding;
    private static final String PREFS_NAME = "MyPrefs";
    private static final String KEY_FIRST_ENTRY_TIME = "firstEntryTime";
    private NavController navController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);

        splashScreen.setKeepOnScreenCondition(() -> keepIt);
        Handler handler = new Handler();
        handler.postDelayed(() ->
                keepIt = false, keepFor);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        long firstEntryTime = sharedPreferences.getLong(KEY_FIRST_ENTRY_TIME, -1);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(binding.navHostFragment.getId());
        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
        }

        if (firstEntryTime == -1) {
            long currentTimeMillis = System.currentTimeMillis();
            sharedPreferences.edit().putLong(KEY_FIRST_ENTRY_TIME, currentTimeMillis).apply();
        } else {
            // Not the first time entering the app, perform other action
            moveToLoginFragment();
        }
    }

    private void moveToLoginFragment() {
        binding.bottomNavigationView.setVisibility(View.VISIBLE);
        navController.navigate(R.id.action_welcome_screen_to_login_screen);
    }

    public void showBottomNavigationView() {
        binding.bottomNavigationView.setVisibility(View.VISIBLE);
    }

    public void hideBottomNavigationView() {
        binding.bottomNavigationView.setVisibility(View.GONE);
    }

}
package com.appdev.gadgetsgalaxy.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;

import com.appdev.gadgetsgalaxy.R;
import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utility {
    private static final String PREFS_NAME_TYPE = "TypePrefs";
    private static final String KEY_USER_TYPE = "userType";
    private static final String PREF_THEME_MODE = "theme_mode";
    private static final String THEME_MODE_LIGHT = "light";
    private static final String THEME_MODE_DARK = "dark";
    public static final String PUBLISHABLE = "pk_test_51PTezcRupmuHuosb5UJBZIpFVqNNso8qakNIYY9kRTULBOqqOqvY6Z3Jw5Wk187bRKx2PJ0jYfBp3MJJi4xASDFU001DEmRDkr";
    public static final String SECRET_KEY = "sk_test_51PTezcRupmuHuosbg4F2vD3DCzV59QEHq4PMFW5hF0Gn1Jg7xvmpUSMtKwGNaEPIWjmjZWwI5ZhomVTxeQ0I8yDn00gOeBYco3";
    @BindingAdapter("imageFromUrl")
    public static void loadImageFromUrl(ImageView imageView, String url) {
        Glide.with(imageView).load(url).placeholder(R.drawable.profileimageph).into(imageView);
    }

    @BindingAdapter("imageFromUrlForOthers")
    public static void loadImageFromUrlForOthers(ImageView imageView, String url) {
        Glide.with(imageView).load(url).placeholder(R.drawable.placeholder).into(imageView);
    }

     public String formatDate(long timestamp) {
        Date date = new Date(timestamp);
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        return sdf.format(date);
    }

    public static String getUserTypeFromPrefs(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME_TYPE, Context.MODE_PRIVATE);
        return prefs.getString(KEY_USER_TYPE, "");
    }


    public static void status_bar(Activity activity, int color) {
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        activity.getWindow().setStatusBarColor(ContextCompat.getColor(activity, color));
    }

    public static void status_bar_dark(Activity activity, int color) {
        Window window = activity.getWindow();

        // Set status bar color
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(activity, color));

        View decorView = window.getDecorView();
        int flags = decorView.getSystemUiVisibility();
        flags &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR; // Remove light status bar flag
        decorView.setSystemUiVisibility(flags);
    }


    public static void setAppTheme(Activity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("theme_prefs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PREF_THEME_MODE, THEME_MODE_LIGHT);
        editor.apply();
    }

    public static void toggleTheme(Activity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("theme_prefs", Activity.MODE_PRIVATE);
        String newThemeMode = sharedPreferences.getString(PREF_THEME_MODE, THEME_MODE_LIGHT).equals(THEME_MODE_LIGHT) ? THEME_MODE_DARK : THEME_MODE_LIGHT;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PREF_THEME_MODE, newThemeMode);
        editor.apply();
        applyTheme(newThemeMode);
    }
    public static String getThemeMode(Activity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("theme_prefs", Activity.MODE_PRIVATE);
        return sharedPreferences.getString(PREF_THEME_MODE, THEME_MODE_LIGHT);
    }

    public static void applyTheme(String themeMode) {
        switch (themeMode) {
            case THEME_MODE_LIGHT:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case THEME_MODE_DARK:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
        }
    }

    public static boolean isDarkModeActivated(Activity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("theme_prefs", Activity.MODE_PRIVATE);
        String currentThemeMode = sharedPreferences.getString(PREF_THEME_MODE, THEME_MODE_LIGHT);
        return currentThemeMode.equals(THEME_MODE_DARK);
    }

}

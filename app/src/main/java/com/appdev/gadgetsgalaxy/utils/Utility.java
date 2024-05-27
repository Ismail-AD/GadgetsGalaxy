package com.appdev.gadgetsgalaxy.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.appdev.gadgetsgalaxy.R;
import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utility {
    private static final String PREFS_NAME_TYPE = "TypePrefs";
    private static final String KEY_USER_TYPE = "userType";
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
}

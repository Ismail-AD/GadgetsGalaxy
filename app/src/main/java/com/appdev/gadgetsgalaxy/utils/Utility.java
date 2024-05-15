package com.appdev.gadgetsgalaxy.utils;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.appdev.gadgetsgalaxy.R;
import com.bumptech.glide.Glide;

public class Utility {
    @BindingAdapter("imageFromUrl")
    public static void loadImageFromUrl(ImageView imageView, String url) {
        Glide.with(imageView).load(url).placeholder(R.drawable.iphonr).into(imageView);
    }
}

package com.appdev.gadgetsgalaxy.data;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.appdev.gadgetsgalaxy.R;
import com.bumptech.glide.Glide;

public class User_info {
    private int cart_items;
    private int ordered_items;
    private String userId;
    private String name;
    private String email;
    private String contact;
    private String address;
    private String imageUrl;

    public User_info(int cart_items, int ordered_items, String userId, String name, String email, String contact, String address, String imageUrl) {
        this.cart_items = cart_items;
        this.ordered_items = ordered_items;
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.contact = contact;
        this.address = address;
        this.imageUrl = imageUrl;
    }

    public User_info() {
    }

    public int getCart_items() {
        return cart_items;
    }

    public void setCart_items(int cart_items) {
        this.cart_items = cart_items;
    }

    public int getOrdered_items() {
        return ordered_items;
    }

    public void setOrdered_items(int ordered_items) {
        this.ordered_items = ordered_items;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}

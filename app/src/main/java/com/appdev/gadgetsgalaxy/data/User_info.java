package com.appdev.gadgetsgalaxy.data;

import android.os.Parcel;
import android.os.Parcelable;

public class User_info implements Parcelable {
    private int cart_items;
    private int ordered_items;
    private String userId;
    private String name;
    private String email;
    private String contact;
    private String address;
    private String imageUrl;
    private String userType;

    protected User_info(Parcel in) {
        cart_items = in.readInt();
        ordered_items = in.readInt();
        userId = in.readString();
        name = in.readString();
        email = in.readString();
        contact = in.readString();
        address = in.readString();
        imageUrl = in.readString();
        userType = in.readString();
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(cart_items);
        dest.writeInt(ordered_items);
        dest.writeString(userId);
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(contact);
        dest.writeString(address);
        dest.writeString(imageUrl);
        dest.writeString(userType);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<User_info> CREATOR = new Creator<User_info>() {
        @Override
        public User_info createFromParcel(Parcel in) {
            return new User_info(in);
        }

        @Override
        public User_info[] newArray(int size) {
            return new User_info[size];
        }
    };

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }



    public User_info(int cart_items, int ordered_items, String userId, String name, String email, String contact, String address, String imageUrl, String userType) {
        this.cart_items = cart_items;
        this.ordered_items = ordered_items;
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.contact = contact;
        this.address = address;
        this.imageUrl = imageUrl;
        this.userType = userType;
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

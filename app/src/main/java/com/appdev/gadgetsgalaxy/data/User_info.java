package com.appdev.gadgetsgalaxy.data;

import android.os.Parcel;
import android.os.Parcelable;

public class User_info implements Parcelable {
    private String postalCode;
    private String userId;
    private String name;
    private String email;
    private String contact;
    private String address;
    private String imageUrl;
    private String userType;

    protected User_info(Parcel in) {
        postalCode = in.readString();
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
        dest.writeString(postalCode);
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



    public User_info(String postalCode, String userId, String name, String email, String contact, String address, String imageUrl, String userType) {
        this.postalCode = postalCode;
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

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
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

package com.appdev.gadgetsgalaxy.data;

import android.os.Parcel;
import android.os.Parcelable;

public class Category_info implements Parcelable {

    private String catTitle;
    private String imageUrl;

    public Category_info(String catTitle, String imageUrl, String catDescription) {
        this.catTitle = catTitle;
        this.imageUrl = imageUrl;
        this.catDescription = catDescription;
    }
    public Category_info() {
    }


    private String catDescription;

    protected Category_info(Parcel in) {
        catTitle = in.readString();
        imageUrl = in.readString();
        catDescription = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(catTitle);
        dest.writeString(imageUrl);
        dest.writeString(catDescription);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Category_info> CREATOR = new Creator<Category_info>() {
        @Override
        public Category_info createFromParcel(Parcel in) {
            return new Category_info(in);
        }

        @Override
        public Category_info[] newArray(int size) {
            return new Category_info[size];
        }
    };

    public String getCatTitle() {
        return catTitle;
    }

    public void setCatTitle(String catTitle) {
        this.catTitle = catTitle;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCatDescription() {
        return catDescription;
    }

    public void setCatDescription(String catDescription) {
        this.catDescription = catDescription;
    }
}

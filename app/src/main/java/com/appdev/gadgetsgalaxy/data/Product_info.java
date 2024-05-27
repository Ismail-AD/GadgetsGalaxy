package com.appdev.gadgetsgalaxy.data;

import android.os.Parcel;
import android.os.Parcelable;

public class Product_info implements Parcelable {
    private String item_name;
    private String imageUrl;
    private String category;
    private int item_price;
    private int item_discounted_price;
    private String productCreated;

    private int model;

    private int selectedQuantity;
    private String item_id;
    private int quantity_available;
    private String item_rating;
    private String desc;

    protected Product_info(Parcel in) {
        item_name = in.readString();
        imageUrl = in.readString();
        item_price = in.readInt();
        item_discounted_price = in.readInt();
        model = in.readInt();
        selectedQuantity = in.readInt();
        item_id = in.readString();
        quantity_available = in.readInt();
        item_rating = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(item_name);
        dest.writeString(imageUrl);
        dest.writeInt(item_price);
        dest.writeInt(item_discounted_price);
        dest.writeInt(model);
        dest.writeInt(selectedQuantity);
        dest.writeString(item_id);
        dest.writeInt(quantity_available);
        dest.writeString(item_rating);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Product_info> CREATOR = new Creator<Product_info>() {
        @Override
        public Product_info createFromParcel(Parcel in) {
            return new Product_info(in);
        }

        @Override
        public Product_info[] newArray(int size) {
            return new Product_info[size];
        }
    };

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getSelectedQuantity() {
        return selectedQuantity;
    }

    public void setSelectedQuantity(int selectedQuantity) {
        this.selectedQuantity = selectedQuantity;
    }

    public int getModel() {
        return model;
    }

    public void setModel(int model) {
        this.model = model;
    }



    public Product_info(){}

    public String getCategory() {
        return category;
    }

    public String getProductCreated() {
        return productCreated;
    }

    public void setProductCreated(String productCreated) {
        this.productCreated = productCreated;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Product_info(String item_name, String imageUrl, int item_price, int model, int item_discounted_price, String item_id, int quantity_available, String item_rating, String desc, String category,String dateCreated) {
        this.item_name = item_name;
        this.imageUrl = imageUrl;
        this.item_price = item_price;
        this.item_discounted_price = item_discounted_price;
        this.item_id = item_id;
        this.model = model;
        this.quantity_available = quantity_available;
        this.item_rating = item_rating;
        this.desc = desc;
        this.category = category;
        this.productCreated = dateCreated;
    }

    public Product_info(String item_name, String imageUrl, int item_price,int model, int item_discounted_price, String item_id, String item_rating, int selectedQuantity,String desc,String category) {
        this.item_name = item_name;
        this.imageUrl = imageUrl;
        this.item_price = item_price;
        this.item_discounted_price = item_discounted_price;
        this.item_id = item_id;
        this.model = model;
        this.item_rating = item_rating;
        this.desc = desc;
        this.selectedQuantity = selectedQuantity;
        this.category = category;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getItem_price() {
        return item_price;
    }

    public void setItem_price(int item_price) {
        this.item_price = item_price;
    }

    public int getItem_discounted_price() {
        return item_discounted_price;
    }

    public void setItem_discounted_price(int item_discounted_price) {
        this.item_discounted_price = item_discounted_price;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public int getQuantity_available() {
        return quantity_available;
    }

    public void setQuantity_available(int quantity_available) {
        this.quantity_available = quantity_available;
    }

    public String getItem_rating() {
        return item_rating;
    }

    public void setItem_rating(String item_rating) {
        this.item_rating = item_rating;
    }
}

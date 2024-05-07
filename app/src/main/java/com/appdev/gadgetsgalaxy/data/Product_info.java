package com.appdev.gadgetsgalaxy.data;

public class Product_info {
    private String item_name;
    private String imageUrl;
    private int item_price;
    private int item_discounted_price;
    private int item_id;
    private int quantity_available;
    private Float item_rating;

    public Product_info(String item_name, String imageUrl, int item_price, int item_discounted_price, int item_id, int quantity_available, Float item_rating) {
        this.item_name = item_name;
        this.imageUrl = imageUrl;
        this.item_price = item_price;
        this.item_discounted_price = item_discounted_price;
        this.item_id = item_id;
        this.quantity_available = quantity_available;
        this.item_rating = item_rating;
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

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public int getQuantity_available() {
        return quantity_available;
    }

    public void setQuantity_available(int quantity_available) {
        this.quantity_available = quantity_available;
    }

    public Float getItem_rating() {
        return item_rating;
    }

    public void setItem_rating(Float item_rating) {
        this.item_rating = item_rating;
    }
}

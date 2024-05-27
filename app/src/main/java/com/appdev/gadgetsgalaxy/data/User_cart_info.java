package com.appdev.gadgetsgalaxy.data;

import java.util.List;

public class User_cart_info {
    private String userId;
    private List<Product_info> cartProducts;
    private int total_price;
    private int deliveryCharges;

    public User_cart_info(String userId, List<Product_info> cartProducts, int total_price, int deliveryCharges) {
        this.userId = userId;
        this.cartProducts = cartProducts;
        this.total_price = total_price;
        this.deliveryCharges = deliveryCharges;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<Product_info> getCartProducts() {
        return cartProducts;
    }

    public void setCartProducts(List<Product_info> cartProducts) {
        this.cartProducts = cartProducts;
    }

    public int getTotal_price() {
        return total_price;
    }

    public void setTotal_price(int total_price) {
        this.total_price = total_price;
    }

    public int getDeliveryCharges() {
        return deliveryCharges;
    }

    public void setDeliveryCharges(int deliveryCharges) {
        this.deliveryCharges = deliveryCharges;
    }
}

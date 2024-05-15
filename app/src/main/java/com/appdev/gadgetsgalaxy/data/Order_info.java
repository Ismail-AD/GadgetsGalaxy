package com.appdev.gadgetsgalaxy.data;

public class Order_info {
    String creationOrDeliveryDate;
    float totalPrice;
    String imageUrl;
    String status;
    String productName;
    int quantity;
    float delivery;
    private String name;
    private String email;
    private String contact;

    public String getCreationOrDeliveryDate() {
        return creationOrDeliveryDate;
    }

    public void setCreationOrDeliveryDate(String creationOrDeliveryDate) {
        this.creationOrDeliveryDate = creationOrDeliveryDate;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getDelivery() {
        return delivery;
    }

    public void setDelivery(float delivery) {
        this.delivery = delivery;
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

    public Order_info(){}
    public Order_info(String creationOrDeliveryDate, float totalPrice, String imageUrl, String status, String productName, int quantity, float delivery, String name, String email, String contact) {
        this.creationOrDeliveryDate = creationOrDeliveryDate;
        this.totalPrice = totalPrice;
        this.imageUrl = imageUrl;
        this.status = status;
        this.productName = productName;
        this.quantity = quantity;
        this.delivery = delivery;
        this.name = name;
        this.email = email;
        this.contact = contact;
    }
}

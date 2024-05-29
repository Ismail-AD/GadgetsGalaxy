package com.appdev.gadgetsgalaxy.data;

public class Order_info {
    private String item_name;
    private String imageUrl;
    private String category;
    private int item_price;
    private int item_discounted_price;

    private int model;
    private int selectedQuantity;
    private String item_id;
    private int quantity_available;
    private String item_rating;
    private String desc;
    private String orderDate; // You can use a long to store the date as a timestamp

    String userId;
    String orderId;
    String status;
    int deliveryPrice;
    private String name;
    private String email;
    private String contact;

    public Order_info(){}

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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public int getModel() {
        return model;
    }

    public void setModel(int model) {
        this.model = model;
    }

    public int getSelectedQuantity() {
        return selectedQuantity;
    }

    public void setSelectedQuantity(int selectedQuantity) {
        this.selectedQuantity = selectedQuantity;
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getDeliveryPrice() {
        return deliveryPrice;
    }

    public void setDeliveryPrice(int deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
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

    public Order_info(String item_name, String imageUrl, String category, int item_price, int item_discounted_price, int model, int selectedQuantity, String item_id, int quantity_available, String item_rating, String desc, String orderDate, String userId, String orderId, String status, int deliveryPrice, String name, String email, String contact) {
        this.item_name = item_name;
        this.imageUrl = imageUrl;
        this.category = category;
        this.item_price = item_price;
        this.item_discounted_price = item_discounted_price;
        this.model = model;
        this.selectedQuantity = selectedQuantity;
        this.item_id = item_id;
        this.quantity_available = quantity_available;
        this.item_rating = item_rating;
        this.desc = desc;
        this.orderDate = orderDate;
        this.userId = userId;
        this.orderId = orderId;
        this.status = status;
        this.deliveryPrice = deliveryPrice;
        this.name = name;
        this.email = email;
        this.contact = contact;
    }
}

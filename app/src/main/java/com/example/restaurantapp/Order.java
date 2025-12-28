package com.example.restaurantapp;

import java.util.List;

public class Order {
    private String customerName;
    private String orderStatus;
    private long timestamp;
    private Double totalPrice; // <-- YAHAN TABDEELI KI GAYI HAI
    private List<FoodItem> items;
    private String orderId;
    private String shippingAddress;

    public Order() {
        // Firebase ke liye khaali constructor zaroori hai
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Double getTotalPrice() { // <-- YAHAN BHI TABDEELI KI GAYI HAI
        return totalPrice;
    }

    public List<FoodItem> getItems() {
        return items;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setTotalPrice(Double totalPrice) { // <-- YAHAN BHI TABDEELI KI GAYI HAI
        this.totalPrice = totalPrice;
    }

    public void setItems(List<FoodItem> items) {
        this.items = items;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }
}

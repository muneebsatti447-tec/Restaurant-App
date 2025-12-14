package com.example.restaurantapp;

import java.util.List;

public class Order {
    private String customerName;
    private String orderStatus;
    private long timestamp;
    private double totalPrice;
    private List<FoodItem> items;
    private String orderId;

    public Order() {
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

    public double getTotalPrice() {
        return totalPrice;
    }

    public List<FoodItem> getItems() {
        return items;
    }

    public String getOrderId() {
        return orderId;
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

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setItems(List<FoodItem> items) {
        this.items = items;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}

package com.example.restaurantapp;

import com.google.firebase.database.Exclude;public class FoodItem {
    private String name;
    private double price;
    private String image;
    private int quantity;

    @Exclude
    private String itemId;

    public void incrementQuantity() {
        this.quantity++;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Exclude
    public int getQuantity() {
        return quantity;
    }

    @Exclude
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Exclude
    public String getItemId() {
        return itemId;
    }

    @Exclude
    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    // Constructors...
    public FoodItem() {
        this.quantity = 1;
    }

    public FoodItem(String name, double price, String image) {
        this.name = name;
        this.price = price;
        this.image = image;
        this.quantity = 1;
    }
}

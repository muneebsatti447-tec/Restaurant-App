package com.example.restaurantapp;import com.google.firebase.database.Exclude;

public class FoodItem {
    private String name;
    private double price;
    private int imageResource;
    private int quantity;

    public FoodItem() {
    }

    public FoodItem(String name, double price, int imageResource) {
        this.name = name;
        this.price = price;
        this.imageResource = imageResource;
        this.quantity = 1;
    }
    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getImageResource() {
        return imageResource;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Exclude
    public void incrementQuantity() {
        this.quantity++;
    }

    @Exclude
    public void decrementQuantity() {
        if (this.quantity > 1) {
            this.quantity--;
        }
    }
}

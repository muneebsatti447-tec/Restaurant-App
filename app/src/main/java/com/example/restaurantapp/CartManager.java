package com.example.restaurantapp;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CartManager {

    private static CartManager instance;
    private List<FoodItem> cartItems = new ArrayList<>();

    private CartManager() {}

    public static synchronized CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    public void addItem(FoodItem newItem) {
        for (FoodItem existingItem : cartItems) {
            if (Objects.equals(existingItem.getName(), newItem.getName())) {
                existingItem.incrementQuantity();
                return;
            }
        }
        cartItems.add(newItem);
    }

    public List<FoodItem> getCartItems() {
        return cartItems;
    }

    public double getTotalPrice() {
        double total = 0.0;
        for (FoodItem item : cartItems) {
            total += item.getPrice() * item.getQuantity();
        }
        return total;
    }

    public void clearCart() {
        cartItems.clear();
    }

    public void removeItem(FoodItem itemToRemove) {
        cartItems.remove(itemToRemove);
    }
}



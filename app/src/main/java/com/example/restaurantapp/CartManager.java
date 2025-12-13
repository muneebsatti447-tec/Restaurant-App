package com.example.restaurantapp;

import java.util.ArrayList;
import java.util.List;

public class CartManager {

    private static CartManager instance;
    private List<FoodItem> cartItems = new ArrayList<>();

    // Private constructor taake iska object bahar se na banaya ja sake
    private CartManager() {}

    // Singleton pattern: Isse poori app mein sirf ek hi CartManager object rahega
    public static synchronized CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    public void addItem(FoodItem item) {
        cartItems.add(item);
    }

    public List<FoodItem> getCartItems() {
        return cartItems;
    }

    public double getTotalPrice() {
        double total = 0.0;
        for (FoodItem item : cartItems) {
            total += item.getPrice();
        }
        return total;
    }

    public void clearCart() {
        cartItems.clear();
    }
}

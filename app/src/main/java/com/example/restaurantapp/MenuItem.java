package com.example.restaurantapp;

public class MenuItem {
    private String itemId;
    private String name;
    private String description;
    private String price;

    public MenuItem() {
    }

    public MenuItem(String itemId, String name, String description, String price) {
        this.itemId = itemId;
        this.name = name;
        this.description = description;
        this.price = price;
    }


    public String getItemId() { return itemId; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getPrice() { return price; }


    public void setItemId(String itemId) { this.itemId = itemId; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setPrice(String price) { this.price = price; }
}

package com.example.restaurantapp;

import android.content.Intent;
import android.os.Bundle;import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity {

    RecyclerView menuRecyclerView;
    MenuAdapter menuAdapter;
    List<FoodItem> foodItemsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);




        menuRecyclerView = findViewById(R.id.menuRecyclerView);
        menuRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        createFoodMenu();

        menuAdapter = new MenuAdapter(this, foodItemsList);

        menuRecyclerView.setAdapter(menuAdapter);
        FloatingActionButton cartFab = findViewById(R.id.cartFab);
        cartFab.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, CartActivity.class);
            startActivity(intent);
        });

    }

    private void createFoodMenu() {
        foodItemsList = new ArrayList<>();
        foodItemsList.add(new FoodItem("Pepperoni Pizza", 1300.00, R.drawable.pizza));
        foodItemsList.add(new FoodItem("Crown Crust Pizza", 1200.00, R.drawable.crown_crust_pizza));
        foodItemsList.add(new FoodItem("Chicken Biryani", 850.00, R.drawable.biryani));
        foodItemsList.add(new FoodItem("Zinger Burger", 600.00, R.drawable.burger));
        foodItemsList.add(new FoodItem("Shawarma", 350.00, R.drawable.shawarma));
        foodItemsList.add(new FoodItem("Paratha Roll", 1300.00, R.drawable.paratha_roll));
        foodItemsList.add(new FoodItem("French Fries", 350.00, R.drawable.fries));
        foodItemsList.add(new FoodItem(" Pasta", 900.00, R.drawable.pasta));
        foodItemsList.add(new FoodItem("Club Sandwich", 500.00, R.drawable.sandwich));
        foodItemsList.add(new FoodItem(" Salad", 750.00, R.drawable.salad));
        foodItemsList.add(new FoodItem("Chicken Roast", 1200.00, R.drawable.chicken));
        foodItemsList.add(new FoodItem("Desi Dall", 800.00, R.drawable.desi_dall));
        foodItemsList.add(new FoodItem("Chicken Karahi", 1800.00, R.drawable.chicken_karahi));
        foodItemsList.add(new FoodItem("chicken_malai_boti", 1000.00, R.drawable.chicken_malai_boti));
        foodItemsList.add(new FoodItem("Beef karahi", 2400.00, R.drawable.beef_karahi));
        foodItemsList.add(new FoodItem("Beef malai boti", 1200.00, R.drawable.beef_malai_boti));
        foodItemsList.add(new FoodItem(" Ice Cream", 400.00, R.drawable.icecream));





    }
}

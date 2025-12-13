package com.example.restaurantapp;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    private RecyclerView cartRecyclerView;
    private CartAdapter cartAdapter;
    private TextView totalBillTextView;
    private CartManager cartManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        cartManager = CartManager.getInstance();
        cartRecyclerView = findViewById(R.id.cartRecyclerView);
        totalBillTextView = findViewById(R.id.totalBillTextView);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<FoodItem> cartItems = cartManager.getCartItems();
        cartAdapter = new CartAdapter(cartItems);
        cartRecyclerView.setAdapter(cartAdapter);
        updateTotalBill();
    }

    private void updateTotalBill() {
        double totalPrice = cartManager.getTotalPrice();
        totalBillTextView.setText(String.format("Total: Rs. %.2f", totalPrice));
    }
}

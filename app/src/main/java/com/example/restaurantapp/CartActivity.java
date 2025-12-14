package com.example.restaurantapp;

import android.content.Intent; // Intent ko import karein
import android.os.Bundle;
import android.view.View;       // View ko import karein
import android.widget.Button;   // Button ko import karein
import android.widget.TextView;
import android.widget.Toast; // Toast ko import karein

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class CartActivity extends AppCompatActivity {

    private RecyclerView cartRecyclerView;
    private CartAdapter cartAdapter;
    private TextView totalBillTextView;
    private CartManager cartManager;
    private List<FoodItem> cartItems;
    private Button proceedButton;

    private FloatingActionButton orderHistoryFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cartManager = CartManager.getInstance();
        cartRecyclerView = findViewById(R.id.cartRecyclerView);
        totalBillTextView = findViewById(R.id.totalBillTextView);
        proceedButton = findViewById(R.id.checkoutButton);
        orderHistoryFab = findViewById(R.id.orderHistoryFab);


        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        cartItems = cartManager.getCartItems();

        cartAdapter = new CartAdapter(cartItems, new CartAdapter.OnItemDeleteListener() {
            @Override
            public void onDeleteClick(int position) {
                if (position >= 0 && position < cartItems.size()) {
                    cartItems.remove(position);
                    cartAdapter.notifyItemRemoved(position);
                    cartAdapter.notifyItemRangeChanged(position, cartItems.size());
                    updateTotalBill();
                }
            }
        });

        cartRecyclerView.setAdapter(cartAdapter);
        updateTotalBill();

        proceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cartItems.isEmpty()) {
                    Toast.makeText(CartActivity.this, "Your cart is empty!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(CartActivity.this, CheckoutActivity.class);
                startActivity(intent);
            }
        });
        orderHistoryFab.setOnClickListener(v -> {

            Intent intent = new Intent(CartActivity.this, OrderHistoryActivity.class);
            startActivity(intent);
        });
    }

    private void updateTotalBill() {
        double totalPrice = 0;
        for (FoodItem item : cartItems) {
            totalPrice += item.getPrice();
        }
        totalBillTextView.setText(String.format("Total: Rs. %.2f", totalPrice));
    }
}

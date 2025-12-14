package com.example.restaurantapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class CheckoutActivity extends AppCompatActivity {

    private static final String TAG = "CheckoutActivity";

    private EditText nameEditText, phoneEditText, houseNoEditText, streetEditText, cityEditText;
    private Button placeOrderButton;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private CartManager cartManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        nameEditText = findViewById(R.id.nameEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        houseNoEditText = findViewById(R.id.houseNoEditText);
        streetEditText = findViewById(R.id.streetEditText);
        cityEditText = findViewById(R.id.cityEditText);
        placeOrderButton = findViewById(R.id.placeOrderButton);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        cartManager = CartManager.getInstance();

        placeOrderButton.setOnClickListener(v -> saveOrderInformation());
    }

    private void saveOrderInformation() {
        String name = nameEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String houseNo = houseNoEditText.getText().toString().trim();
        String street = streetEditText.getText().toString().trim();
        String city = cityEditText.getText().toString().trim();

        if (name.isEmpty() || phone.isEmpty() || houseNo.isEmpty() || street.isEmpty() || city.isEmpty()) {
            Toast.makeText(this, "Please fill all the delivery details", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "You must be logged in to place an order.", Toast.LENGTH_LONG).show();
            return;
        }

        String userId = currentUser.getUid();
        String fullAddress = houseNo + ", " + street + ", " + city;

        List<FoodItem> orderItems = cartManager.getCartItems();
        double totalPrice = cartManager.getTotalPrice();

        if (orderItems.isEmpty()) {
            Toast.makeText(this, "Your cart is empty. Please add items to proceed.", Toast.LENGTH_SHORT).show();
            return;
        }

        HashMap<String, Object> orderData = new HashMap<>();
        orderData.put("customerName", name);
        orderData.put("phoneNumber", phone);
        orderData.put("shippingAddress", fullAddress);
        orderData.put("orderStatus", "Pending");
        orderData.put("timestamp", System.currentTimeMillis());
        orderData.put("totalPrice", totalPrice);
        orderData.put("items", orderItems);

        DatabaseReference newOrderRef = mDatabase.child("Orders").child(userId).push();
        newOrderRef.setValue(orderData).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(CheckoutActivity.this, "Order placed successfully!", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Order placed successfully for user: " + userId);

                cartManager.clearCart();
                Intent intent = new Intent(CheckoutActivity.this, ThankYouActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            } else {
                String errorMessage = Objects.requireNonNull(task.getException()).getMessage();
                Toast.makeText(CheckoutActivity.this, "Failed to place order: " + errorMessage, Toast.LENGTH_LONG).show();
                Log.e(TAG, "Failed to write order to database", task.getException());
            }
        });
    }
}

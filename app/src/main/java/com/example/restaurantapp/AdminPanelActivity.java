package com.example.restaurantapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminPanelActivity extends AppCompatActivity {

    private TextView tvTotalRevenue, tvTotalUsers, tvTotalOrders, tvMenuItems;
    private DatabaseReference usersRef, ordersRef, menuItemsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);


        tvTotalRevenue = findViewById(R.id.tvTotalRevenue);
        tvTotalUsers = findViewById(R.id.tvTotalUsers);
        tvTotalOrders = findViewById(R.id.tvTotalOrders);
        tvMenuItems = findViewById(R.id.tvMenuItems);


        usersRef = FirebaseDatabase.getInstance().getReference("Users");
        ordersRef = FirebaseDatabase.getInstance().getReference("Orders");
        menuItemsRef = FirebaseDatabase.getInstance().getReference("MenuItems");


        fetchDashboardData();


        findViewById(R.id.btnManageMenu).setOnClickListener(v ->
                startActivity(new Intent(this, ViewItemsActivity.class))
        );

        findViewById(R.id.btnManageOrders).setOnClickListener(v -> {
                    startActivity(new Intent(this, OrderDetailsActivity.class));
                }
        );

        findViewById(R.id.btnLogout).setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void fetchDashboardData() {

        usersRef.orderByChild("role").equalTo("customer")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        long userCount = snapshot.exists() ? snapshot.getChildrenCount() : 0;
                        tvTotalUsers.setText(String.valueOf(userCount));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(AdminPanelActivity.this,
                                "Failed to load user data", Toast.LENGTH_SHORT).show();
                    }
                });


        ordersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long totalOrders = 0;
                double totalRevenue = 0.0;


                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot orderSnapshot : userSnapshot.getChildren()) {
                        totalOrders++;

                        String status = orderSnapshot.child("orderStatus").getValue(String.class);
                        if (status != null && status.equalsIgnoreCase("Delivered")) {
                            Double price = orderSnapshot.child("totalPrice").getValue(Double.class);
                            if (price != null) totalRevenue += price;
                        }
                    }
                }

                tvTotalOrders.setText(String.valueOf(totalOrders));
                tvTotalRevenue.setText("Rs " + String.format("%.2f", totalRevenue));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AdminPanelActivity.this,
                        "Failed to load order data", Toast.LENGTH_SHORT).show();
            }
        });


        menuItemsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long menuCount = snapshot.exists() ? snapshot.getChildrenCount() : 0;
                tvMenuItems.setText(String.valueOf(menuCount));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AdminPanelActivity.this,
                        "Failed to load menu data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

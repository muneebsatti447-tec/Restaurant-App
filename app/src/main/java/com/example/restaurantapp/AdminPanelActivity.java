package com.example.restaurantapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class AdminPanelActivity extends AppCompatActivity {

    // tvMenuItems ko ab tvPendingOrders ke taur par istemal karenge
    private TextView tvTotalRevenue, tvTotalUsers, tvTotalOrders, tvPendingOrders;
    private DatabaseReference usersRef, ordersRef; // menuItemsRef ki ab yahan zaroorat nahin

    private RecyclerView rvOrders;
    private OrderAdapter orderAdapter;
    private List<Order> orderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);

        // Views ko initialize karein
        tvTotalRevenue = findViewById(R.id.tvTotalRevenue);
        tvTotalUsers = findViewById(R.id.tvTotalUsers);
        tvTotalOrders = findViewById(R.id.tvTotalOrders);
        // XML mein id abhi bhi tvMenuItems ho sakti hai, lekin hum isay Pending Orders ke liye istemal karenge
        tvPendingOrders = findViewById(R.id.tvMenuItems);

        // Firebase References
        usersRef = FirebaseDatabase.getInstance().getReference("Users");
        ordersRef = FirebaseDatabase.getInstance().getReference("Orders");

        // Data fetch karein
        fetchDashboardData();

        // RecyclerView setup karein
        setupOrdersRecyclerView();
        fetchAllOrdersForList();

        findViewById(R.id.btnLogout).setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void setupOrdersRecyclerView() {
        rvOrders = findViewById(R.id.rvOrders);
        rvOrders.setLayoutManager(new LinearLayoutManager(this));
        orderList = new ArrayList<>();
        orderAdapter = new OrderAdapter(orderList);
        rvOrders.setAdapter(orderAdapter);
    }

    private void fetchAllOrdersForList() {
        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orderList.clear();
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot orderSnapshot : userSnapshot.getChildren()) {
                        Order order = orderSnapshot.getValue(Order.class);
                        if (order != null) {
                            order.setOrderId(orderSnapshot.getKey());
                            orderList.add(order);
                        }
                    }
                }
                // Orders ko naye se purane ki tarteeb mein sort karein (timestamp ke hisaab se)
                Collections.sort(orderList, (o1, o2) -> Long.compare(o2.getTimestamp(), o1.getTimestamp()));
                orderAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AdminPanelActivity.this, "Failed to load orders list.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ===== YAHAN AHEM TABDEELI KI GAYI HAI =====
    private void fetchDashboardData() {
        // Users ka count fetch karein
        usersRef.orderByChild("role").equalTo("customer")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        long userCount = snapshot.exists() ? snapshot.getChildrenCount() : 0;
                        tvTotalUsers.setText(String.valueOf(userCount));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(AdminPanelActivity.this, "Failed to load user data", Toast.LENGTH_SHORT).show();
                    }
                });

        // Orders ka data fetch karein (Total Orders, Total Revenue, aur Pending Orders)
        ordersRef.addValueEventListener(new ValueEventListener() { // addListenerForSingleValueEvent se addValueEventListener kiya
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long totalOrders = 0;
                long pendingOrders = 0; // Pending orders ke liye naya counter
                double totalRevenue = 0.0;

                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot orderSnapshot : userSnapshot.getChildren()) {
                        totalOrders++;

                        String status = orderSnapshot.child("orderStatus").getValue(String.class);

                        // Pending orders ka count karein
                        if (status != null && status.equalsIgnoreCase("Pending")) {
                            pendingOrders++;
                        }

                        // Delivered orders se revenue calculate karein
                        if (status != null && status.equalsIgnoreCase("Delivered")) {
                            Double price = orderSnapshot.child("totalPrice").getValue(Double.class);
                            if (price != null) {
                                totalRevenue += price;
                            }
                        }
                    }
                }

                // UI update karein
                tvTotalOrders.setText(String.valueOf(totalOrders));
                tvTotalRevenue.setText("Rs " + String.format(Locale.getDefault(), "%.2f", totalRevenue));
                tvPendingOrders.setText(String.valueOf(pendingOrders)); // tvMenuItems ki jagah ab pending orders ka count
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AdminPanelActivity.this, "Failed to load order data", Toast.LENGTH_SHORT).show();
            }
        });

        // menuItemsRef wala block poora hata diya gaya hai.
    }
}

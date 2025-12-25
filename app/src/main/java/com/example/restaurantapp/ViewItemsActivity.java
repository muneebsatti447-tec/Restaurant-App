package com.example.restaurantapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class ViewItemsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FoodAdapter adapter;
    private List<FoodItem> foodList;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_items);

        recyclerView = findViewById(R.id.recyclerViewItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        foodList = new ArrayList<>();

        // Adapter setup with click listener
        adapter = new FoodAdapter(this, foodList, new FoodAdapter.OnItemClickListener() {
            @Override
            public void onEditClick(FoodItem item) {
                // TODO: Handle Edit click
                Toast.makeText(ViewItemsActivity.this, "Edit: " + item.getName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDeleteClick(FoodItem item) {
                // Delete confirmation
                new AlertDialog.Builder(ViewItemsActivity.this)
                        .setTitle("Delete Item")
                        .setMessage("Are you sure you want to delete '" + item.getName() + "'?")
                        .setPositiveButton("Yes", (dialog, which) -> deleteItem(item.getName()))
                        .setNegativeButton("No", null)
                        .show();
            }
        });
        recyclerView.setAdapter(adapter);


        databaseReference = FirebaseDatabase.getInstance().getReference("FoodItems");
        fetchFoodItems();

        // FAB to add new item
        FloatingActionButton fab = findViewById(R.id.fabAddItem);
        fab.setOnClickListener(v -> startActivity(new Intent(ViewItemsActivity.this, AddItemActivity.class)));
    }

    private void fetchFoodItems() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                foodList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    FoodItem item = dataSnapshot.getValue(FoodItem.class);
                    if (item != null) {
                        foodList.add(item);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewItemsActivity.this, "Failed to load items.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteItem(String itemId) {
        databaseReference.child(itemId).removeValue()
                .addOnSuccessListener(aVoid -> Toast.makeText(ViewItemsActivity.this, "Item deleted.", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(ViewItemsActivity.this, "Failed to delete item.", Toast.LENGTH_SHORT).show());
    }
}

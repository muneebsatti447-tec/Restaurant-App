package com.example.restaurantapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddItemActivity extends AppCompatActivity {

    private EditText etItemName, etItemDescription, etItemPrice;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_item);


        etItemName = findViewById(R.id.etItemName);
        etItemDescription = findViewById(R.id.etItemDescription);
        etItemPrice = findViewById(R.id.etItemPrice);
        Button btnSaveItem = findViewById(R.id.btnSaveItem);


        databaseReference = FirebaseDatabase.getInstance().getReference("MenuItems");


        btnSaveItem.setOnClickListener(v -> saveMenuItem());
    }

    private void saveMenuItem() {
        String name = etItemName.getText().toString().trim();
        String description = etItemDescription.getText().toString().trim();
        String price = etItemPrice.getText().toString().trim();


        if (TextUtils.isEmpty(name)) {
            etItemName.setError("Name is required");
            return;
        }
        if (TextUtils.isEmpty(description)) {
            etItemDescription.setError("Description is required");
            return;
        }
        if (TextUtils.isEmpty(price)) {
            etItemPrice.setError("Price is required");
            return;
        }

        String itemId = databaseReference.push().getKey();


        MenuItem menuItem = new MenuItem(itemId, name, description, price);


        if (itemId != null) {
            databaseReference.child(itemId).setValue(menuItem)
                    .addOnSuccessListener(aVoid -> {

                        Toast.makeText(AddItemActivity.this, "Item Added Successfully!", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> {

                        Toast.makeText(AddItemActivity.this, "Failed to add item: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }
}

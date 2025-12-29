package com.example.restaurantapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;

public class AddItemActivity extends AppCompatActivity {

    private ImageView ivImagePreview;
    private TextInputEditText etItemName, etItemPrice;
    private Button btnSaveItem;

    private DatabaseReference menuDatabaseReference;
    private Uri imageUri;


    private final ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {

                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    imageUri = result.getData().getData();
                    ivImagePreview.setImageURI(imageUri);
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        ivImagePreview = findViewById(R.id.ivImagePreview);
        etItemName = findViewById(R.id.etItemName);
        etItemPrice = findViewById(R.id.etItemPrice);
        btnSaveItem = findViewById(R.id.btnSaveItem);

        menuDatabaseReference = FirebaseDatabase.getInstance().getReference("MenuItems");
        ivImagePreview.setOnClickListener(v -> openGallery());
        btnSaveItem.setOnClickListener(v -> saveMenuItemToFirebase());
    }


    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryLauncher.launch(galleryIntent);
    }


    private void saveMenuItemToFirebase() {
        String name = etItemName.getText().toString().trim();
        String priceStr = etItemPrice.getText().toString().trim();


        if (name.isEmpty()) {
            etItemName.setError("Item name is required");
            etItemName.requestFocus();
            return;
        }

        if (priceStr.isEmpty()) {
            etItemPrice.setError("Price is required");
            etItemPrice.requestFocus();
            return;
        }

        if (imageUri == null) {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
            return;
        }


        String imageBase64 = convertImageToBase64(ivImagePreview);
        if (imageBase64 == null) {
            Toast.makeText(this, "Could not convert image. Please try again.", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double price = Double.parseDouble(priceStr);

            FoodItem newItem = new FoodItem(name, price, imageBase64);


            menuDatabaseReference.push().setValue(newItem).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(AddItemActivity.this, "Item added successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AddItemActivity.this, "Failed to add item: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } catch (NumberFormatException e) {
            etItemPrice.setError("Invalid price format");
            etItemPrice.requestFocus();
        }
    }

    private String convertImageToBase64(ImageView imageView) {
        try {
            Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);
            byte[] byteArray = baos.toByteArray();
            return Base64.encodeToString(byteArray, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

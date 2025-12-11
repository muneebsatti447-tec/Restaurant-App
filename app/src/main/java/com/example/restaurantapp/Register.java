package com.example.restaurantapp; // Make sure this matches your package name

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Register extends AppCompatActivity {

    // 1. Declare UI elements and Firebase Auth
    private EditText emailEditText, passwordEditText;
    private Button registerButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        // This handles padding for the system bars (status bar, navigation bar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 2. Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // 3. Connect UI elements with their IDs from the XML
        emailEditText = findViewById(R.id.editTextTextEmailAddress2);
        passwordEditText = findViewById(R.id.editTextNumberPassword2);
        registerButton = findViewById(R.id.button2);

        // 4. Set a click listener on the register button
        registerButton.setOnClickListener(v -> {
            // Call the method to create an account
            createAccount();
        });
    }

    private void createAccount() {
        // Get email and password from the EditText fields
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // 5. Validate the input
        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Email is required.");
            emailEditText.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Password is required.");
            passwordEditText.requestFocus();
            return;
        }

        if (password.length() < 6) {
            passwordEditText.setError("Password must be at least 6 characters long.");
            passwordEditText.requestFocus();
            return;
        }

        // 6. Use Firebase to create a new user with email and password
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Registration successful
                        Toast.makeText(Register.this, "Registration Successful!", Toast.LENGTH_SHORT).show();

                        // 7. Navigate back to MainActivity
                        Intent intent = new Intent(Register.this, MainActivity.class);
                        // Clear the activity stack to prevent user from going back to register screen
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish(); // Close the RegisterActivity

                    } else {
                        // If registration fails, display a message to the user.
                        // You can check the specific exception to give a more detailed error
                        Toast.makeText(Register.this, "Registration Failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}

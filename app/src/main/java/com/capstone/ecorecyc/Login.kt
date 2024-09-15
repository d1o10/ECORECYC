package com.capstone.ecorecyc

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {

    // Firebase authentication instance
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val signupbtn: Button = findViewById(R.id.Signup)
        signupbtn.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            intent.putExtra("USER_TYPE", "USER")
            startActivity(intent)
        }

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        val loginButton: Button = findViewById(R.id.btn_login) // This is your Sign-in button
        val emailField: EditText = findViewById(R.id.email)
        val passwordField: EditText = findViewById(R.id.password)

        // Handle login button click
        loginButton.setOnClickListener {
            val email = emailField.text.toString().trim()
            val password = passwordField.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email or Password cannot be empty", Toast.LENGTH_SHORT).show()
            } else {
                // Sign in user with Firebase Auth
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                            // Navigate to the main app activity after login
                            val intent = Intent(this, Dashboard::class.java)
                            startActivity(intent)
                            finish() // Optional: Prevent the user from returning to the login screen
                        } else {
                            // Login failed
                            Toast.makeText(this, "Login failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }
}

package com.capstone.ecorecyc

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {

    // Firebase authentication instance
    private lateinit var auth: FirebaseAuth

    // SharedPreferences to store email
    private lateinit var sharedPreferences: SharedPreferences
    private val PREFS_NAME = "LoginPrefs"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val signupbtn: Button = findViewById(R.id.Signup)
        signupbtn.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            intent.putExtra("USER_TYPE", "USER")
            startActivity(intent)
        }

        val forgotbuttonn: Button = findViewById(R.id.forgotbtn)
        forgotbuttonn.setOnClickListener {
            val intent = Intent(this, ForgotPassword::class.java)
            intent.putExtra("USER_TYPE", "USER")
            startActivity(intent)
        }

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        val loginButton: Button = findViewById(R.id.btn_login)
        val emailField: EditText = findViewById(R.id.email)
        val passwordField: EditText = findViewById(R.id.password)
        val rememberMeCheckBox: CheckBox = findViewById(R.id.remembermeCheckbox)

        // Check if the user has checked "Remember Me" previously and set the email field
        val rememberedEmail = sharedPreferences.getString("remembered_email", null)
        if (rememberedEmail != null) {
            emailField.setText(rememberedEmail)
            rememberMeCheckBox.isChecked = true
        }

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

                            // Save email in SharedPreferences if "Remember Me" is checked
                            if (rememberMeCheckBox.isChecked) {
                                val editor = sharedPreferences.edit()
                                editor.putString("remembered_email", email)
                                editor.apply() // Save the email
                            } else {
                                // If "Remember Me" is unchecked, clear the saved email
                                val editor = sharedPreferences.edit()
                                editor.remove("remembered_email")
                                editor.apply()
                            }

                            // Navigate to the main app activity after login
                            val intent = Intent(this, Navbar::class.java)
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

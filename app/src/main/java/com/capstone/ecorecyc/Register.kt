package com.capstone.ecorecyc

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.InputType
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.capstone.ecorecyc.R.drawable.visibility_off
import com.capstone.ecorecyc.R.drawable.visibility_on
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Register : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var progressBar: ProgressBar
    private var isPasswordVisible = false // Track password visibility state

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Initialize Firebase Auth, Firestore, and ProgressBar
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        progressBar = findViewById(R.id.progress_bar)
        progressBar.visibility = View.GONE // Hide progress bar initially

        val usernameField: EditText = findViewById(R.id.username)
        val emailField: EditText = findViewById(R.id.email)
        val passwordField: EditText = findViewById(R.id.password)

        // Set up password visibility toggle
        setUpPasswordVisibilityToggle(passwordField)

        val registerButton: Button = findViewById(R.id.btn_login)
        registerButton.setOnClickListener {
            val username = usernameField.text.toString().trim()
            val email = emailField.text.toString().trim()
            val password = passwordField.text.toString().trim()

            if (email.isEmpty() || password.isEmpty() || username.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            } else {
                // Show the progress bar when user clicks sign up button
                progressBar.visibility = View.VISIBLE

                // Create a new user
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val userId = auth.currentUser?.uid
                            val user = hashMapOf(
                                "username" to username,
                                "email" to email
                            )
                            userId?.let {
                                firestore.collection("users").document(it).set(user)
                                    .addOnSuccessListener {
                                        Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()

                                        // Hide the progress bar after success
                                        progressBar.visibility = View.GONE

                                        // Navigate to login
                                        val intent = Intent(this, Login::class.java)
                                        startActivity(intent)
                                        finish()
                                    }
                                    .addOnFailureListener { e ->
                                        Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                                        // Hide the progress bar on error
                                        progressBar.visibility = View.GONE
                                    }
                            }
                        } else {
                            Toast.makeText(this, "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                            // Hide the progress bar on error
                            progressBar.visibility = View.GONE
                        }
                    }
            }
        }

        val login2btn: Button = findViewById(R.id.loginregister)
        login2btn.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }
    }

    // Function to set up password visibility toggle
    private fun setUpPasswordVisibilityToggle(passwordField: EditText) {
        // Define the drawable icons for password visibility states
        val visibilityOnIcon: Drawable? = ContextCompat.getDrawable(this, visibility_on)
        val visibilityOffIcon: Drawable? = ContextCompat.getDrawable(this, visibility_off)

        // Get the existing start drawable (password icon) from the XML
        val passwordIcon: Drawable? = ContextCompat.getDrawable(this, R.drawable.password_icon)

        // Set the initial drawable (password hidden state) with the start icon
        passwordField.setCompoundDrawablesWithIntrinsicBounds(passwordIcon, null, visibilityOffIcon, null)

        // Set an OnTouchListener on the EditText to detect drawable end click
        passwordField.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                // Get drawable end width and check if the touch is within its bounds
                val drawableEnd = passwordField.compoundDrawables[2] // drawableRight (visibility icon)
                if (drawableEnd != null && event.rawX >= (passwordField.right - drawableEnd.bounds.width())) {
                    // Toggle password visibility
                    isPasswordVisible = !isPasswordVisible
                    if (isPasswordVisible) {
                        // Show password
                        passwordField.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                        passwordField.setCompoundDrawablesWithIntrinsicBounds(passwordIcon, null, visibilityOnIcon, null)
                    } else {
                        // Hide password
                        passwordField.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                        passwordField.setCompoundDrawablesWithIntrinsicBounds(passwordIcon, null, visibilityOffIcon, null)
                    }
                    // Move the cursor to the end of the text after toggling
                    passwordField.setSelection(passwordField.text.length)
                    return@setOnTouchListener true // Consume the touch event
                }
            }
            return@setOnTouchListener false // Pass the event to other listeners if not handled
        }
    }
}

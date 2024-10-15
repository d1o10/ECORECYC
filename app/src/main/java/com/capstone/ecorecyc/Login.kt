package com.capstone.ecorecyc

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.InputType
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.capstone.ecorecyc.R.drawable.visibility_off
import com.capstone.ecorecyc.R.drawable.visibility_on
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {

    // Firebase authentication instance
    private lateinit var auth: FirebaseAuth

    // SharedPreferences to store email
    private lateinit var sharedPreferences: SharedPreferences
    private val PREFS_NAME = "LoginPrefs"

    // ProgressBar for loading indication
    private lateinit var progressBar: ProgressBar

    // Track if password is visible
    private var isPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        // Initialize ProgressBar and hide it initially
        progressBar = findViewById(R.id.progress_bar)
        progressBar.visibility = View.GONE

        // UI Elements
        val emailField: EditText = findViewById(R.id.email)
        val passwordField: EditText = findViewById(R.id.password)
        val rememberMeCheckBox: CheckBox = findViewById(R.id.remembermeCheckbox)
        val loginButton: Button = findViewById(R.id.btn_login)
        val signupBtn: Button = findViewById(R.id.Signup)
        val forgotPasswordBtn: Button = findViewById(R.id.forgotbtn)

        // Handle "Sign Up" button click
        signupBtn.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            intent.putExtra("USER_TYPE", "USER")
            startActivity(intent)
        }

        // Handle "Forgot Password" button click
        forgotPasswordBtn.setOnClickListener {
            val intent = Intent(this, ForgotPassword::class.java)
            intent.putExtra("USER_TYPE", "USER")
            startActivity(intent)
        }

        // Retrieve saved email if "Remember Me" was checked
        val rememberedEmail = sharedPreferences.getString("remembered_email", null)
        if (rememberedEmail != null) {
            emailField.setText(rememberedEmail)
            rememberMeCheckBox.isChecked = true
        }

        // Add functionality to toggle password visibility on drawable end click
        setUpPasswordVisibilityToggle(passwordField)

        // Handle login button click
        loginButton.setOnClickListener {
            val email = emailField.text.toString().trim()
            val password = passwordField.text.toString().trim()

            // Basic validation
            if (email.isEmpty()) {
                emailField.error = "Email is required"
                emailField.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                passwordField.error = "Password is required"
                passwordField.requestFocus()
                return@setOnClickListener
            }

            // Show progress while logging in
            progressBar.visibility = View.VISIBLE

            // Authenticate with Firebase
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    progressBar.visibility = View.GONE // Hide progress bar after login attempt

                    if (task.isSuccessful) {
                        // Successful login
                        Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()

                        // Save email in SharedPreferences if "Remember Me" is checked
                        val editor = sharedPreferences.edit()
                        if (rememberMeCheckBox.isChecked) {
                            editor.putString("remembered_email", email)
                        } else {
                            editor.remove("remembered_email")
                        }
                        editor.apply()

                        // Navigate to the main app activity
                        val intent = Intent(this, Navbar::class.java)
                        startActivity(intent)
                        finish() // Prevent the user from returning to the login screen
                    } else {
                        // Failed login
                        val errorMessage = task.exception?.localizedMessage ?: "Login failed"
                        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                }
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

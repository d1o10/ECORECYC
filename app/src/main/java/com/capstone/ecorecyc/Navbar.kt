package com.capstone.ecorecyc

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class Navbar : AppCompatActivity() {

    private var doubleBackToExitPressedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navbar)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        // Load the default fragment (DashboardFragment)
        loadFragment(DashboardFragment())

        bottomNav.setOnItemSelectedListener { item ->
            var selectedFragment: Fragment? = null
            when (item.itemId) {
                R.id.homeIcon -> selectedFragment = DashboardFragment()
                R.id.cartIcon -> selectedFragment = CartFragment()
                R.id.chatIcon -> selectedFragment = ChatFragment()  // Assuming this fragment exists
                R.id.profileIcon -> selectedFragment = UserProfileFragment()
            }

            if (selectedFragment != null) {
                loadFragment(selectedFragment)
            }
            true
        }
    }

    private fun loadFragment(fragment: Fragment) {
        // Replace the fragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            // Log out the user if back is pressed twice
            FirebaseAuth.getInstance().signOut()
            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show()
            finishAffinity()  // Close the app
        } else {
            // Set the flag to true and show the confirmation message
            this.doubleBackToExitPressedOnce = true
            Toast.makeText(this, "Do you want to log out?", Toast.LENGTH_SHORT).show()

            // Reset the flag after 2 seconds
            Handler(Looper.getMainLooper()).postDelayed({
                doubleBackToExitPressedOnce = false
            }, 2000)
        }
    }
}

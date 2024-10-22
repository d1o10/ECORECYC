package com.capstone.ecorecyc

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class Navbar : AppCompatActivity() {

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
}

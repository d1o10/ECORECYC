package com.capstone.ecorecyc

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import android.widget.ImageButton

class MyOrders : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_orders)

        // Step 1: Set up ViewPager2 and TabLayout
        val viewPager: ViewPager2 = findViewById(R.id.viewPager)
        val tabLayout: TabLayout = findViewById(R.id.tabLayout)

        // Step 2: Initialize fragments for each tab
        val orderFragments = listOf(
            OrderOngoingFragment(), // Fragment for Ongoing Orders
            OrderCompletedFragment() // Fragment for Completed Orders
        )

        // Step 3: Set up the adapter
        viewPager.adapter = MyOrdersPagerAdapter(this, orderFragments)

        // Step 4: Connect TabLayout with ViewPager2
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Ongoing" // Tab title for Ongoing Orders
                1 -> "Completed" // Tab title for Completed Orders
                else -> ""
            }
        }.attach()

        // Step 5: Set up the back button listener
        val backButton: ImageButton = findViewById(R.id.my_orders_back_button)
        backButton.setOnClickListener {
            // Navigate back to UserProfileFragment
            finish() // Close the MyOrders activity and return to the previous activity
        }
    }
}

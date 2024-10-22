package com.capstone.ecorecyc

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment

class DashboardFragment : Fragment() {

    private lateinit var sharedPreferences: SharedPreferences
    private val PREFS_NAME = "LoginPrefs" // Use the same name you used in the Register activity
    private lateinit var greetingTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        // Initialize SharedPreferences
        sharedPreferences = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        // Set up greeting
        greetingTextView = view.findViewById(R.id.hiusername)
        updateGreeting() // Set the greeting text

        // Find and set up your buttons
        val market: ImageButton = view.findViewById(R.id.marketplacebutton)
        market.setOnClickListener {
            val intent = Intent(activity, Marketplace::class.java)
            intent.putExtra("USER_TYPE", "USER")
            startActivity(intent)
        }

        val recyclingbtn: ImageButton = view.findViewById(R.id.recylinghubbutton)
        recyclingbtn.setOnClickListener {
            val intent = Intent(activity, RecyclingHub::class.java)
            intent.putExtra("USER_TYPE", "USER")
            startActivity(intent)
        }

        val cleanbtn: ImageButton = view.findViewById(R.id.cleanupbtn)
        cleanbtn.setOnClickListener {
            val intent = Intent(activity, CleanupEvents::class.java)
            intent.putExtra("USER_TYPE", "USER")
            startActivity(intent)
        }

        // Add the chat button functionality
        val chatBtn: ImageButton = view.findViewById(R.id.notificationBtn)
        chatBtn.setOnClickListener {
            val intent = Intent(activity, Chat::class.java)
            startActivity(intent)
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        updateGreeting() // Reload username when the fragment resumes
    }

    // Method to update the greeting text
    private fun updateGreeting() {
        val username = sharedPreferences.getString("username", "User") // Default to "User" if not found
        greetingTextView.text = "$username!"
    }
}

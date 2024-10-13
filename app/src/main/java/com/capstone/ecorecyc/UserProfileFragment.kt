package com.capstone.ecorecyc

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth

class UserProfileFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_user_profile, container, false)

        // Initialize FirebaseAuth
        auth = FirebaseAuth.getInstance()

        // Back button to navigate to DashboardFragment
        val backBtn: ImageButton = view.findViewById(R.id.backbtnprofile)
        backBtn.setOnClickListener {
            val intent = Intent(activity, DashboardFragment::class.java)
            intent.putExtra("USER_TYPE", "USER")
            startActivity(intent)
        }

        // Logout button to navigate to Login and display a toast
        val logoutBtn: ImageButton = view.findViewById(R.id.logout_btn)
        logoutBtn.setOnClickListener {
            auth.signOut() // Sign out the user
            Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show()
            val intent = Intent(activity, Login::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            activity?.finish() // Close the current activity
        }

        return view
    }
}

package com.capstone.ecorecyc

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UserProfileFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var profileName: TextView
    private lateinit var profileEmail: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_user_profile, container, false)

        // Initialize Firebase Auth and Firestore
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Get references to TextViews
        profileName = view.findViewById(R.id.profile_name)
        profileEmail = view.findViewById(R.id.profile_email)

        // Get the current user and fetch email/username from Firestore
        val currentUser = auth.currentUser
        currentUser?.let {
            profileEmail.text = it.email

            // Fetch user details (username) from Firestore
            val userId = it.uid
            db.collection("users").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val username = document.getString("username")
                        profileName.text = username
                    } else {
                        Log.d("UserProfileFragment", "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("UserProfileFragment", "Failed to retrieve data: ", exception)
                }
        }

        // Settings button to navigate to EditProfile activity
        val sett: ImageButton = view.findViewById(R.id.settings_btn)
        sett.setOnClickListener {
            val intent = Intent(activity, EditProfile::class.java)
            intent.putExtra("USER_TYPE", "USER")
            startActivity(intent)
        }

        // Logout button to sign out the user, display a toast, and navigate to Login activity
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

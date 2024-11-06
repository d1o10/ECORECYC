package com.capstone.ecorecyc

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.bumptech.glide.load.resource.bitmap.CircleCrop

class UserProfileFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var profileName: TextView
    private lateinit var profileEmail: TextView
    private lateinit var profileImageView: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_profile, container, false)

        // Initialize Firebase Auth and Firestore
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Get references to TextViews and ImageView
        profileName = view.findViewById(R.id.profile_name)
        profileEmail = view.findViewById(R.id.profile_email)
        profileImageView = view.findViewById(R.id.profile_image)

        // Load the current user profile
        loadUserProfile()

        // Settings button to navigate to Settings activity
        val settingsBtn: ImageButton = view.findViewById(R.id.settings_btn)
        settingsBtn.setOnClickListener {
            val intent = Intent(activity, Settings::class.java)
            startActivity(intent)
        }

        // Logout button to sign out the user
        val logoutBtn: ImageButton = view.findViewById(R.id.logout_btn)
        logoutBtn.setOnClickListener {
            auth.signOut() // Sign out the user
            Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show()
            val intent = Intent(activity, Login::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            activity?.finish() // Close the current activity
        }

        // My Orders button to navigate to My Orders activity
        val myOrdersBtn: ImageButton = view.findViewById(R.id.my_orders_btn)
        myOrdersBtn.setOnClickListener {
            // Launch MyOrders Activity
            val intent = Intent(activity, MyOrders::class.java)
            startActivity(intent)  // Start the activity
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        loadUserProfile()  // Reload user profile whenever the fragment is resumed
    }

    private fun loadUserProfile() {
        val currentUser = auth.currentUser
        currentUser?.let {
            db.collection("users").document(it.uid).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        profileEmail.text = document.getString("email") ?: it.email
                        val displayName = document.getString("displayName") ?: "No Name"
                        profileName.text = displayName

                        val photoUrl = document.getString("photoUrl")
                        if (!photoUrl.isNullOrEmpty()) {
                            Glide.with(this)
                                .load(photoUrl)
                                .transform(CircleCrop())
                                .into(profileImageView)
                        } else {
                            Glide.with(this)
                                .load(R.drawable.img_5) // Default image if no profile photo
                                .transform(CircleCrop())
                                .into(profileImageView)
                        }
                    } else {
                        Log.d("UserProfileFragment", "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w("UserProfileFragment", "Error getting documents: ", exception)
                }
        }
    }

    companion object {
        private const val REQUEST_CODE_EDIT_PROFILE = 1001
    }
}

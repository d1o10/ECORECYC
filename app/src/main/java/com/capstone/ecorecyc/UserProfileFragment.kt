package com.capstone.ecorecyc

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

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
        // Inflate the layout for this fragment
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

        // Settings button to navigate to EditProfile activity
        val sett: Button = view.findViewById(R.id.settingarrowbtn)
        sett.setOnClickListener {
            val intent = Intent(activity, Settings::class.java)
            intent.putExtra("USER_TYPE", "USER")
            startActivityForResult(intent, REQUEST_CODE_EDIT_PROFILE)
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

        return view
    }

    override fun onResume() {
        super.onResume()
        loadUserProfile()  // Reload user profile whenever the fragment is resumed
    }

    private fun loadUserProfile() {
        val currentUser = auth.currentUser
        currentUser?.let {
            // Fetch additional user details from Firestore
            db.collection("users").document(it.uid).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        profileEmail.text = document.getString("email") ?: it.email
                        profileName.text = document.getString("displayName") ?: it.displayName
                        val photoUrl = document.getString("photoUrl") ?: it.photoUrl.toString()
                        Glide.with(this).load(photoUrl).into(profileImageView)
                    } else {
                        Log.d("UserProfileFragment", "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w("UserProfileFragment", "Error getting documents: ", exception)
                }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_EDIT_PROFILE && resultCode == Activity.RESULT_OK && data != null) {
            // Update the profile with the new data
            val newName = data.getStringExtra("name")
            val newEmail = data.getStringExtra("email")
            val newImageUri = data.getStringExtra("imageUri")

            profileName.text = newName
            profileEmail.text = newEmail
            if (newImageUri != null) {
                Glide.with(this).load(Uri.parse(newImageUri)).into(profileImageView)
            }
        }
    }

    companion object {
        private const val REQUEST_CODE_EDIT_PROFILE = 1001
    }
}

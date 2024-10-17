package com.capstone.ecorecyc

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.IOException

class EditProfile : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var profileImageView: ImageView
    private lateinit var editTextName: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var saveProfileBtn: Button
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        // Initialize Firebase Auth and Firestore
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        profileImageView = findViewById(R.id.profile_image)
        editTextName = findViewById(R.id.editTextName)
        editTextEmail = findViewById(R.id.editTextEmail)
        saveProfileBtn = findViewById(R.id.save_profile_btn)

        // Load existing user profile data
        loadUserProfile()

        // Set click listener for the profile image
        profileImageView.setOnClickListener {
            pickImageFromGallery()
        }

        // Set click listener for the save button
        saveProfileBtn.setOnClickListener {
            saveProfile()
        }
    }

    private fun loadUserProfile() {
        val currentUser = auth.currentUser
        currentUser?.let {
            editTextName.setText(it.displayName)
            editTextEmail.setText(it.email)
            Glide.with(this).load(it.photoUrl).into(profileImageView)
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            imageUri = data?.data
            profileImageView.setImageURI(imageUri)
        }
    }

    private fun saveProfile() {
        val name = editTextName.text.toString()
        val email = editTextEmail.text.toString()

        if (imageUri != null) {
            val storageRef = FirebaseStorage.getInstance().reference.child("profile_images/${auth.currentUser?.uid}")
            storageRef.putFile(imageUri!!)
                .addOnSuccessListener {
                    storageRef.downloadUrl.addOnSuccessListener { uri ->
                        updateProfileData(name, email, uri.toString())
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Error uploading image: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            updateProfileData(name, email)
        }
    }

    private fun updateProfileData(name: String, email: String, imageUrl: String? = null) {
        val user = auth.currentUser
        val updates = hashMapOf<String, Any>()
        updates["displayName"] = name
        updates["email"] = email
        if (imageUrl != null) {
            updates["photoUrl"] = imageUrl
        }

        // Update Firestore
        db.collection("users").document(user?.uid ?: "")
            .set(updates)
            .addOnSuccessListener {
                Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                setResult(Activity.RESULT_OK, Intent().apply {
                    putExtra("name", name)
                    putExtra("email", email)
                    putExtra("imageUri", imageUrl)
                })
                finish() // Close EditProfile activity
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error updating profile: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    companion object {
        private const val REQUEST_CODE_PICK_IMAGE = 1000
    }
}

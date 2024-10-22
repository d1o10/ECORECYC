package com.capstone.ecorecyc

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

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

        // Initialize views
        profileImageView = findViewById(R.id.edit_profile_image)
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
            db.collection("users").document(it.uid).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        editTextName.setText(document.getString("displayName"))
                        editTextEmail.setText(document.getString("email"))

                        val photoUrl = document.getString("photoUrl")
                        if (!photoUrl.isNullOrEmpty()) {
                            // Load the profile image
                            Glide.with(this)
                                .load(photoUrl)
                                .transform(CircleCrop())
                                .into(profileImageView)
                        }
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to load profile", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE)
    }

    private fun saveProfile() {
        val name = editTextName.text.toString()
        val email = editTextEmail.text.toString()

        if (name.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val user = auth.currentUser
        val updates = hashMapOf<String, Any>(
            "displayName" to name,
            "email" to email
        )

        if (imageUri != null) {
            uploadImageToFirebase(imageUri!!, user!!.uid, updates)
        } else {
            // Include existing photoUrl when updating if no new image is uploaded
            db.collection("users").document(user!!.uid).get().addOnSuccessListener { document ->
                if (document != null) {
                    val existingPhotoUrl = document.getString("photoUrl")
                    if (!existingPhotoUrl.isNullOrEmpty()) {
                        updates["photoUrl"] = existingPhotoUrl // Keep the existing image URL
                    }
                    updateProfileData(updates)
                }
            }
        }

        // Save updated username in SharedPreferences
        val sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("username", name) // Update the username
        editor.apply() // Commit changes
    }

    private fun uploadImageToFirebase(imageUri: Uri, userId: String, updates: HashMap<String, Any>) {
        val storageRef = FirebaseStorage.getInstance().reference.child("profile_images/$userId")

        storageRef.putFile(imageUri)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    updates["photoUrl"] = uri.toString()
                    updateProfileData(updates)
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Image upload failed", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateProfileData(updates: HashMap<String, Any>) {
        val user = auth.currentUser

        db.collection("users").document(user?.uid ?: "")
            .set(updates)
            .addOnSuccessListener {
                Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                setResult(Activity.RESULT_OK, Intent().apply {
                    putExtra("name", updates["displayName"] as? String)
                    putExtra("email", updates["email"] as? String)
                    putExtra("imageUri", updates["photoUrl"] as? String)
                })
                finish()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error updating profile: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.data
            Glide.with(this).load(imageUri).transform(CircleCrop()).into(profileImageView)
        }
    }

    companion object {
        private const val REQUEST_CODE_PICK_IMAGE = 1002
    }
}

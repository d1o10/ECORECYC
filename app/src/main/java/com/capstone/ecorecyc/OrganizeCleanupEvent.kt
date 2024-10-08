package com.capstone.ecorecyc

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class OrganizeCleanupEvent : AppCompatActivity() {

    private lateinit var cleanupName: EditText
    private lateinit var location: EditText
    private lateinit var calendar: CalendarView
    private lateinit var description: EditText
    private lateinit var uploadPhotoButton: Button
    private lateinit var confirmButton: Button
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_organize_cleanup_event)

        cleanupName = findViewById(R.id.cleanup_Name)
        location = findViewById(R.id.location)
        calendar = findViewById(R.id.date_picker_actions)
        description = findViewById(R.id.cleanup_description)
        uploadPhotoButton = findViewById(R.id.upload_btn)
        confirmButton = findViewById(R.id.confirm_cleanup_event_btn)

        // Handle image upload
        uploadPhotoButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 100)
        }

        // Handle confirm button click
        confirmButton.setOnClickListener {
            uploadEventToFirebase()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK) {
            imageUri = data?.data
            Toast.makeText(this, "Image selected", Toast.LENGTH_SHORT).show()
        }
    }

    private fun uploadEventToFirebase() {
        val storageRef = FirebaseStorage.getInstance().reference.child("event_images/${UUID.randomUUID()}")
        imageUri?.let { uri ->
            storageRef.putFile(uri).addOnSuccessListener { taskSnapshot ->
                storageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    saveEventDetails(downloadUri.toString())
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show()
            }
        } ?: saveEventDetails("") // If no image is selected
    }

    private fun saveEventDetails(imageUrl: String) {
        val event = hashMapOf(
            "cleanupName" to cleanupName.text.toString(),
            "location" to location.text.toString(),
            "date" to calendar.date.toString(),
            "description" to description.text.toString(),
            "imageUrl" to imageUrl
        )

        val databaseRef = FirebaseDatabase.getInstance().getReference("cleanup_events")
        val eventId = databaseRef.push().key

        if (eventId != null) {
            databaseRef.child(eventId).setValue(event).addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(this, "Event successfully organized", Toast.LENGTH_SHORT).show()
                    // Navigate back to CleanupEvents after success
                    val intent = Intent(this, CleanupEvents::class.java)
                    startActivity(intent)
                    finish() // Finish the current activity to prevent returning to it
                } else {
                    Toast.makeText(this, "Failed to organize event", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

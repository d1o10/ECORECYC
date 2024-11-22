package com.capstone.ecorecyc

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class VolunteerCleanupEvent : AppCompatActivity() {

    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_volunteer_cleanup_event)

        val imageView: ImageView = findViewById(R.id.event_img)
        val imageUrl = intent.getStringExtra("IMAGE_URL")
        val eventId = intent.getStringExtra("EVENT_ID")

        // Check if image URL is passed correctly
        if (imageUrl != null && imageUrl.isNotEmpty()) {
            Glide.with(this)
                .load(imageUrl)
                .into(imageView)
        } else {
            Toast.makeText(this, "No image URL provided", Toast.LENGTH_SHORT).show()
        }

        val confirmButton: Button = findViewById(R.id.confirm_volunteer_event_btn)
        confirmButton.setOnClickListener {
            val volunteerName = findViewById<EditText>(R.id.volunteer_Name).text.toString().trim()
            val location = findViewById<EditText>(R.id.location).text.toString().trim()
            val age = findViewById<EditText>(R.id.age).text.toString().trim()
            val gender = findViewById<EditText>(R.id.gender).text.toString().trim()

            // Check if all fields are filled out
            if (volunteerName.isNotEmpty() && location.isNotEmpty() && age.isNotEmpty() && gender.isNotEmpty()) {
                if (eventId != null) {
                    addParticipant(eventId, volunteerName, location, age, gender)
                } else {
                    Toast.makeText(this, "Event ID not found", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addParticipant(eventId: String, name: String, location: String, age: String, gender: String) {
        val participant = hashMapOf(
            "name" to name,
            "location" to location,
            "age" to age,
            "gender" to gender
        )

        firestore.collection("cleanup_events")
            .document(eventId)
            .collection("participants")
            .add(participant)
            .addOnSuccessListener {
                Toast.makeText(this, "Successfully Joined!", Toast.LENGTH_SHORT).show()

                // Add notification after joining
                val notificationService = NotificationService()
                val userId = FirebaseAuth.getInstance().currentUser?.uid
                userId?.let {
                    notificationService.addNotification(it, "$name has joined the cleanup event!")
                }

                finish()  // Close the activity and return to the previous one
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}

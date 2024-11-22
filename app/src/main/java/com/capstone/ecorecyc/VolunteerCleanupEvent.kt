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

        // Load event image if URL is provided
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

            // Ensure all fields are filled
            if (volunteerName.isNotEmpty() && location.isNotEmpty() && age.isNotEmpty() && gender.isNotEmpty()) {
                if (eventId != null) {
                    addParticipantAndNotifyCreator(eventId, volunteerName, location, age, gender)
                } else {
                    Toast.makeText(this, "Event ID not found", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addParticipantAndNotifyCreator(eventId: String, name: String, location: String, age: String, gender: String) {
        // Add participant to the event's participants subcollection
        val participant = hashMapOf(
            "name" to name,
            "location" to location,
            "age" to age,
            "gender" to gender
        )

        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show()
            return
        }

        val userId = currentUser.uid

        // Get the event document to retrieve the creatorId
        firestore.collection("cleanup_events")
            .document(eventId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val creatorId = document.getString("creatorId")
                    val eventName = document.getString("name") ?: "Event"

                    if (creatorId != null) {
                        // Add participant to Firestore
                        firestore.collection("cleanup_events")
                            .document(eventId)
                            .collection("participants")
                            .add(participant)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Successfully Joined!", Toast.LENGTH_SHORT).show()

                                // Create a notification for the creator
                                val notification = hashMapOf(
                                    "message" to "$name has joined your event '$eventName'",
                                    "timestamp" to System.currentTimeMillis()
                                )

                                firestore.collection("users")
                                    .document(creatorId)
                                    .collection("notifications")
                                    .add(notification)
                                    .addOnSuccessListener {
                                        Toast.makeText(this, "The event creator has been notified!", Toast.LENGTH_SHORT).show()
                                    }
                                    .addOnFailureListener { e ->
                                        Toast.makeText(this, "Failed to notify the creator: ${e.message}", Toast.LENGTH_SHORT).show()
                                    }

                                finish() // Close the activity after success
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        Toast.makeText(this, "Event creator not found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Event not found.", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to fetch event: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}

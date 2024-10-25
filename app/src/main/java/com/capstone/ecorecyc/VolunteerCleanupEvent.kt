package com.capstone.ecorecyc

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore

class VolunteerCleanupEvent : AppCompatActivity() {

    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_volunteer_cleanup_event)

        val imageView: ImageView = findViewById(R.id.event_img)
        val imageUrl = intent.getStringExtra("IMAGE_URL")
        val eventId = intent.getStringExtra("EVENT_ID")

        if (imageUrl != null) {
            Glide.with(this)
                .load(imageUrl)
                .into(imageView)
        }

        val confirmButton: Button = findViewById(R.id.confirm_volunteer_event_btn)
        confirmButton.setOnClickListener {
            val volunteerName = findViewById<EditText>(R.id.volunteer_Name).text.toString().trim()
            val location = findViewById<EditText>(R.id.location).text.toString().trim()
            val age = findViewById<EditText>(R.id.age).text.toString().trim()
            val gender = findViewById<EditText>(R.id.gender).text.toString().trim()

            if (volunteerName.isNotEmpty() && location.isNotEmpty() && age.isNotEmpty() && gender.isNotEmpty()) {
                addParticipant(eventId, volunteerName, location, age, gender)
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addParticipant(eventId: String?, name: String, location: String, age: String, gender: String) {
        if (eventId == null) {
            Toast.makeText(this, "Event ID not found", Toast.LENGTH_SHORT).show()
            return
        }

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
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
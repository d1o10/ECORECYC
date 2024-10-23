package com.capstone.ecorecyc

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class VolunteerCleanupEvent : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_volunteer_cleanup_event)

        val imageView: ImageView = findViewById(R.id.event_img)
        val imageUrl = intent.getStringExtra("IMAGE_URL")

        if (imageUrl != null) {
            Glide.with(this)
                .load(imageUrl)
                .into(imageView)
        }

        val confirmButton: Button = findViewById(R.id.confirm_volunteer_event_btn)
        confirmButton.setOnClickListener {
            val volunteerName = findViewById<EditText>(R.id.volunteer_Name).text.toString()
            val location = findViewById<EditText>(R.id.location).text.toString()
            val age = findViewById<EditText>(R.id.age).text.toString()
            val gender = findViewById<EditText>(R.id.gender).text.toString()

            if (volunteerName.isNotEmpty() && location.isNotEmpty() && age.isNotEmpty() && gender.isNotEmpty()) {
                Toast.makeText(this, "Successfully Joined!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
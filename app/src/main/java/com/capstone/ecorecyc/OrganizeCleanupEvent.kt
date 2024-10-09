package com.capstone.ecorecyc

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide // Import Glide
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class OrganizeCleanupEvent : AppCompatActivity() {

    private lateinit var cleanupNameEditText: EditText
    private lateinit var locationEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var dateTextView: TextView
    private lateinit var timeTextView: TextView
    private lateinit var uploadButton: Button
    private lateinit var confirmButton: Button
    private lateinit var imageView: ImageView // Declare ImageView

    private var imageUri: Uri? = null
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_organize_cleanup_event)

        cleanupNameEditText = findViewById(R.id.cleanup_Name)
        locationEditText = findViewById(R.id.location)
        descriptionEditText = findViewById(R.id.cleanup_description)
        dateTextView = findViewById(R.id.date_picker)
        timeTextView = findViewById(R.id.time_picker)
        uploadButton = findViewById(R.id.upload_btn)
        confirmButton = findViewById(R.id.confirm_cleanup_event_btn)
        imageView = findViewById(R.id.imageView) // Initialize ImageView

        // Register the launcher for selecting images
        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                imageUri = data?.data // Get the image URI
                if (imageUri != null) {
                    // Display the selected image in the ImageView
                    Glide.with(this) // Use Glide to load the image
                        .load(imageUri)
                        .into(imageView)
                    Toast.makeText(this, "Image Selected", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Set up button listeners
        uploadButton.setOnClickListener {
            openGallery() // Open the gallery to select an image
        }

        confirmButton.setOnClickListener {
            uploadEvent() // Upload the event to Firebase
        }

        // Date picker dialog for selecting date
        dateTextView.setOnClickListener {
            showDatePicker()
        }

        // Time picker dialog for selecting time
        timeTextView.setOnClickListener {
            showTimePicker()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        resultLauncher.launch(intent)
    }

    private fun uploadEvent() {
        val name = cleanupNameEditText.text.toString().trim()
        val loc = locationEditText.text.toString().trim()
        val desc = descriptionEditText.text.toString().trim()
        val date = dateTextView.text.toString().trim()
        val time = timeTextView.text.toString().trim()

        if (name.isEmpty() || loc.isEmpty() || desc.isEmpty() || date.isEmpty() || time.isEmpty() || imageUri == null) {
            Toast.makeText(this, "Please fill all fields and select an image", Toast.LENGTH_SHORT).show()
            return
        }

        val storageRef = FirebaseStorage.getInstance().getReference("cleanup_events/${UUID.randomUUID()}.jpg")
        storageRef.putFile(imageUri!!)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                    // Create an Event object
                    val event = Event(
                        cleanupName = name,
                        location = loc,
                        date = date,
                        time = time,
                        description = desc,
                        imageUrl = downloadUrl.toString()
                    )

                    // Save event to Firebase Realtime Database
                    val databaseRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("cleanup_events")
                    val eventId = databaseRef.push().key
                    databaseRef.child(eventId!!).setValue(event)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this, "Event Created", Toast.LENGTH_SHORT).show()
                                finish() // Navigate back to the previous activity
                            } else {
                                Toast.makeText(this, "Failed to create event", Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Image Upload Failed", Toast.LENGTH_SHORT).show()
            }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            // Format the date and set it to the TextView
            val selectedDate = "${selectedDay}/${selectedMonth + 1}/$selectedYear"
            dateTextView.text = selectedDate
        }, year, month, day)

        datePickerDialog.show()
    }

    private fun showTimePicker() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(this, { _, selectedHour, selectedMinute ->
            // Format the time and set it to the TextView
            val selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
            timeTextView.text = selectedTime
        }, hour, minute, true)

        timePickerDialog.show()
    }
}

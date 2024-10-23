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
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class OrganizeCleanupEvent : AppCompatActivity() {

    private lateinit var cleanupNameEditText: EditText
    private lateinit var locationEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var dateTextView: TextView
    private lateinit var timeTextView: TextView
    private lateinit var uploadButton: Button
    private lateinit var confirmButton: Button
    private lateinit var inviteButton: Button
    private lateinit var imageView: ImageView

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
        inviteButton = findViewById(R.id.invitePeople_btn)
        imageView = findViewById(R.id.imageView)

        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                imageUri = data?.data
                if (imageUri != null) {
                    Glide.with(this)
                        .load(imageUri)
                        .into(imageView)
                    Toast.makeText(this, "Image Selected", Toast.LENGTH_SHORT).show()
                }
            }
        }

        uploadButton.setOnClickListener {
            openGallery()
        }

        confirmButton.setOnClickListener {
            uploadEvent()
        }

        inviteButton.setOnClickListener {
            shareEventLink()
        }

        dateTextView.setOnClickListener {
            showDatePicker()
        }

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

        val storageRef = FirebaseStorage.getInstance().getReference("event_photos/${UUID.randomUUID()}.jpg")
        storageRef.putFile(imageUri!!)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                    val event = hashMapOf(
                        "cleanupName" to name,
                        "location" to loc,
                        "date" to date,
                        "time" to time,
                        "description" to desc,
                        "imageUrl" to downloadUrl.toString()
                    )

                    FirebaseFirestore.getInstance().collection("cleanup_events")
                        .add(event)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this, "Event Created", Toast.LENGTH_SHORT).show()
                                finish()
                            } else {
                                Toast.makeText(this, "Failed to create event: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                }.addOnFailureListener {
                    Toast.makeText(this, "Failed to retrieve image URL", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Image Upload Failed", Toast.LENGTH_SHORT).show()
            }
    }

    private fun shareEventLink() {
        val name = cleanupNameEditText.text.toString().trim()
        val loc = locationEditText.text.toString().trim()
        val date = dateTextView.text.toString().trim()
        val time = timeTextView.text.toString().trim()

        if (name.isEmpty() || loc.isEmpty() || date.isEmpty() || time.isEmpty() || imageUri == null) {
            Toast.makeText(this, "Please complete the event details and upload an image", Toast.LENGTH_SHORT).show()
            return
        }

        val storageRef = FirebaseStorage.getInstance().getReference("event_photos/${UUID.randomUUID()}.jpg")
        storageRef.putFile(imageUri!!)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                    val eventLink = "Join our clean-up event!\n\n" +
                            "Event: $name\n" +
                            "Location: $loc\n" +
                            "Date: $date\n" +
                            "Time: $time\n" +
                            "Check out the event image here: $downloadUrl\n" +
                            "For more info, contact me!"

                    val shareIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, eventLink)
                    }

                    startActivity(Intent.createChooser(shareIntent, "Invite via"))
                }.addOnFailureListener {
                    Toast.makeText(this, "Failed to retrieve image URL", Toast.LENGTH_SHORT).show()
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
            val selectedDate = "${selectedDay}/${selectedMonth + 1}/$selectedYear"
            dateTextView.text = selectedDate
        }, year, month, day)

        datePickerDialog.show()
    }

    private fun showTimePicker() {
        var startTime: String = ""
        var endTime: String = ""

        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val startTimePickerDialog = TimePickerDialog(this, { _, selectedHour, selectedMinute ->
            val isPM = selectedHour >= 12
            val formattedHour = if (selectedHour % 12 == 0) 12 else selectedHour % 12
            val formattedTime = String.format("%02d:%02d %s", formattedHour, selectedMinute, if (isPM) "PM" else "AM")
            startTime = formattedTime

            val endTimePickerDialog = TimePickerDialog(this, { _, endHour, endMinute ->
                val isEndPM = endHour >= 12
                val formattedEndHour = if (endHour % 12 == 0) 12 else endHour % 12
                val formattedEndTime = String.format("%02d:%02d %s", formattedEndHour, endMinute, if (isEndPM) "PM" else "AM")
                endTime = formattedEndTime

                timeTextView.text = "$startTime - $endTime"
            }, hour, minute, false)
            endTimePickerDialog.show()

        }, hour, minute, false)
        startTimePickerDialog.show()
    }
}
package com.capstone.ecorecyc

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Notifications : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var notificationAdapter: NotificationAdapter
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)

        recyclerView = findViewById(R.id.notifications_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
        if (currentUserId == null) {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show()
            return
        }

        fetchNotifications(currentUserId)
    }

    private fun fetchNotifications(userId: String) {
        firestore.collection("users")
            .document(userId)  // The current user
            .collection("notifications")  // Subcollection where notifications are stored
            .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)  // Order by timestamp
            .get()
            .addOnSuccessListener { documents ->
                val notifications = documents.mapNotNull {
                    val message = it.getString("message") ?: "Unknown notification"
                    val timestamp = it.getLong("timestamp") ?: 0L
                    Notification(message, timestamp)  // Convert Firestore data to Notification object
                }

                if (notifications.isEmpty()) {
                    Toast.makeText(this, "No notifications", Toast.LENGTH_SHORT).show()
                } else {
                    notificationAdapter = NotificationAdapter(notifications)
                    recyclerView.adapter = notificationAdapter
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Failed to fetch notifications: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }
}

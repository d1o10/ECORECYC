package com.capstone.ecorecyc

import com.google.firebase.firestore.FirebaseFirestore

class NotificationService {

    private val firestore = FirebaseFirestore.getInstance()

    // Function to add a notification for a user
    fun addNotification(userId: String, message: String) {
        val notification = hashMapOf(
            "message" to message,
            "timestamp" to System.currentTimeMillis()
        )

        firestore.collection("users")
            .document(userId)
            .collection("notifications")
            .add(notification)
            .addOnSuccessListener {
                // Handle success (e.g., log or show a toast)
            }
            .addOnFailureListener {
                // Handle failure (e.g., log error)
            }
    }

    // Function to notify the event creator when someone joins the event
    fun notifyEventCreator(eventId: String, participantId: String) {
        // Fetch the event document to get the creatorId
        firestore.collection("cleanup_events")
            .document(eventId)
            .get()
            .addOnSuccessListener { document ->
                val creatorId = document.getString("creatorId")
                creatorId?.let {
                    val message = "User $participantId has joined your event."
                    addNotification(it, message)
                }
            }
            .addOnFailureListener {
                // Handle failure (e.g., log error)
            }
    }

    // Function to notify participants when they join an event
    fun notifyParticipant(eventId: String, participantId: String) {
        firestore.collection("cleanup_events")
            .document(eventId)
            .collection("participants")
            .document(participantId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val message = "You have successfully joined the event."
                    addNotification(participantId, message)
                }
            }
            .addOnFailureListener {
                // Handle failure (e.g., log error)
            }
    }
}

package com.capstone.ecorecyc

import com.google.firebase.firestore.FirebaseFirestore

class NotificationService {

    private val firestore = FirebaseFirestore.getInstance()

    fun addNotification(userId: String, message: String) {
        val notification = hashMapOf(
            "message" to message,
            "timestamp" to System.currentTimeMillis()
        )

        firestore.collection("users")
            .document(userId)
            .collection("notifications")
            .add(notification)
    }
}

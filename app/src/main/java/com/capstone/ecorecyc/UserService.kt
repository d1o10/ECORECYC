package com.capstone.ecorecyc

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging

class UserService {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    fun storeFcmToken() {
        val userId = auth.currentUser?.uid ?: return

        FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
            db.collection("users")
                .document(userId)
                .update("fcmToken", token)
        }
    }
}

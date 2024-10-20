package com.capstone.ecorecyc

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

object CartManager {
    private val cartItems = mutableListOf<Data.Item>()
    private val db = FirebaseFirestore.getInstance()

    fun addItem(item: Data.Item) {
        cartItems.add(item)
        saveToFirestore(item) // Save item to Firestore
    }

    fun getCartItems(): List<Data.Item> {
        return cartItems
    }

    private fun saveToFirestore(item: Data.Item) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            db.collection("users").document(userId).collection("cart").add(item)
                .addOnSuccessListener {
                    // Optionally handle success
                }
                .addOnFailureListener { e ->
                    // Optionally handle failure
                }
        }
    }

    // Add a method to load items from Firestore
    fun loadCartItems(callback: (List<Data.Item>) -> Unit) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            db.collection("users").document(userId).collection("cart").get()
                .addOnSuccessListener { documents ->
                    val items = documents.mapNotNull { document ->
                        document.toObject(Data.Item::class.java)
                    }
                    callback(items)
                }
                .addOnFailureListener { e ->
                    // Optionally handle failure
                    callback(emptyList())
                }
        } else {
            callback(emptyList())
        }
    }

    fun clearCart() {
        cartItems.clear()
    }
}

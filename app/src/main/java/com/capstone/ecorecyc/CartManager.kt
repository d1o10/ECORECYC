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

    fun deleteItemFromFirestore(item: Data.Item) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            db.collection("users").document(userId).collection("cart")
                .whereEqualTo("name", item.name).get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        db.collection("users").document(userId).collection("cart")
                            .document(document.id).delete()
                            .addOnSuccessListener {
                                // Remove the item from the local cart
                                cartItems.remove(item)
                            }
                            .addOnFailureListener { e ->
                                // Handle failure to delete
                            }
                    }
                }
                .addOnFailureListener { e ->
                    // Handle failure to fetch documents
                }
        }
    }

    fun loadCartItems(onItemsLoaded: (List<Data.Item>) -> Unit) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            db.collection("users").document(userId).collection("cart").get()
                .addOnSuccessListener { documents ->
                    val items = documents.mapNotNull { it.toObject(Data.Item::class.java) }
                    cartItems.clear() // Clear the local cart before loading new items
                    cartItems.addAll(items) // Add loaded items to the local cart
                    onItemsLoaded(items)
                }
                .addOnFailureListener { e ->
                    // Handle failure to load items
                    onItemsLoaded(emptyList()) // Return an empty list in case of failure
                }
        } else {
            // Handle case where user is not authenticated
            onItemsLoaded(emptyList())
        }
    }
}

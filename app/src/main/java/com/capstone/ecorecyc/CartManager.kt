package com.capstone.ecorecyc

object CartManager {
    private val cartItems = mutableListOf<Data.Item>()

    fun addItem(item: Data.Item) {
        cartItems.add(item)
    }

    fun getItems(): List<Data.Item> {
        return cartItems
    }

    fun getCartItems(): List<Data.Item> { // New method added
        return getItems() // Return the existing getItems() result
    }

    fun clearCart() {
        cartItems.clear()
    }

    // Other management functions (removeItem, etc.) can be added here
}

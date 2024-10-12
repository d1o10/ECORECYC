package com.capstone.ecorecyc

object CartManager {

    // List to hold cart items
    private val cartItems = mutableListOf<Data.Item>()

    // Function to add an item to the cart
    fun addItemToCart(name: String?, price: String?, imageUrl: String?) {
        val item = Data.Item(name ?: "", price ?: "", imageUrl ?: "")
        cartItems.add(item)
    }

    // Function to return all items in the cart
    fun getCartItems(): List<Data.Item> {
        return cartItems
    }

    // Function to remove an item from the cart (optional)
    fun removeItemFromCart(item: Data.Item) {
        cartItems.remove(item)
    }

    // Function to clear the cart
    fun clearCart() {
        cartItems.clear()
    }

    // Function to get the total number of items in the cart (optional)
    fun getCartItemCount(): Int {
        return cartItems.size
    }
}

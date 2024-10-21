package com.capstone.ecorecyc

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CartActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var cartAdapter: CartAdapter
    private var cartItems: MutableList<Data.Item> = mutableListOf() // Use MutableList for item removal

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart) // Ensure this matches your layout file

        recyclerView = findViewById(R.id.cartRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Load items into cartItems (this should come from your CartManager)
        loadCartItems()
    }

    private fun loadCartItems() {
        // Assuming you have a CartManager that loads the cart items
        CartManager.loadCartItems { items ->
            cartItems = items.toMutableList() // Update the cartItems list
            cartAdapter = CartAdapter(cartItems) { item ->
                removeItem(item) // Handle item deletion
            }
            recyclerView.adapter = cartAdapter // Set the adapter to the RecyclerView
        }
    }

    private fun removeItem(item: Data.Item) {
        cartItems.remove(item) // Remove the item from the list
        cartAdapter.notifyDataSetChanged() // Notify the adapter of the change
    }
}

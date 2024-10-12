package com.capstone.ecorecyc

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstone.ecorecyc.databinding.ActivityCartBinding // Import the generated binding class

class CartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCartBinding // Declare the binding variable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCartBinding.inflate(layoutInflater) // Inflate the binding
        setContentView(binding.root) // Set the content view to the root of the binding

        // Set up the RecyclerView
        binding.cartRecyclerView.layoutManager = LinearLayoutManager(this)

        // Get cart items from CartManager
        val cartItems = CartManager.getCartItems()

        // Set the adapter for the RecyclerView
        val cartAdapter = MarketplaceAdapter(cartItems)
        binding.cartRecyclerView.adapter = cartAdapter
    }
}

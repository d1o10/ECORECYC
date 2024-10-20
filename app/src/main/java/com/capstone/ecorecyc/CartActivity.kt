package com.capstone.ecorecyc

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.ecorecyc.databinding.ActivityCartBinding

class CartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up the RecyclerView
        binding.cartRecyclerView.layoutManager = LinearLayoutManager(this)

        // Load cart items from Firestore
        CartManager.loadCartItems { cartItems ->
            // Set the adapter for the RecyclerView with loaded items
            val cartAdapter = CartAdapter(cartItems)
            binding.cartRecyclerView.adapter = cartAdapter
        }
    }
}

package com.capstone.ecorecyc

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CartFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CartAdapter
    private val cartItems = mutableListOf<Data.Item>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cart, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Load cart items from Firestore
        CartManager.loadCartItems { items ->
            cartItems.clear()
            cartItems.addAll(items)
            adapter = CartAdapter(cartItems) { item ->
                removeItem(item) // Handle item deletion
            }
            recyclerView.adapter = adapter
        }

        // Handle proceed to checkout
        val checkoutButton: Button = view.findViewById(R.id.btn_proceed_to_checkout)
        checkoutButton.setOnClickListener {
            proceedToCheckout() // Handle checkout process
        }

        return view
    }

    private fun removeItem(item: Data.Item) {
        cartItems.remove(item)
        CartManager.deleteItemFromFirestore(item)
        adapter.notifyDataSetChanged()
        Toast.makeText(context, "Item removed from cart", Toast.LENGTH_SHORT).show()
    }

    private fun proceedToCheckout() {
        if (cartItems.isEmpty()) {
            Toast.makeText(context, "Cart is empty", Toast.LENGTH_SHORT).show()
        } else {
            // Proceed with checkout logic (e.g., payment or order processing)
            Toast.makeText(context, "Proceeding to checkout...", Toast.LENGTH_SHORT).show()
            // You can add navigation to a new CheckoutFragment or activity here
        }
    }
}

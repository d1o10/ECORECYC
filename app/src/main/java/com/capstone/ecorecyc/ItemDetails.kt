package com.capstone.ecorecyc

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class ItemDetails : AppCompatActivity() {

    private lateinit var itemImage: ImageView
    private lateinit var itemName: TextView
    private lateinit var itemPrice: TextView
    private lateinit var itemDescription: TextView
    private lateinit var itemCondition: TextView
    private lateinit var itemLocation: TextView
    private lateinit var addToCartButton: Button
    private lateinit var buyNowButton: Button
    private lateinit var backBtn: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_details)

        // Initialize views
        itemImage = findViewById(R.id.itemImage)
        itemName = findViewById(R.id.itemName)
        itemPrice = findViewById(R.id.itemPrice)
        itemDescription = findViewById(R.id.itemDescription)
        itemCondition = findViewById(R.id.itemCondition)
        itemLocation = findViewById(R.id.itemLocation)
        addToCartButton = findViewById(R.id.addToCartButton)
        buyNowButton = findViewById(R.id.buyNowButton)
        backBtn = findViewById(R.id.backBtn)

        // Get the item details from the intent
        val name = intent.getStringExtra("name") ?: ""
        val price = intent.getStringExtra("price") ?: ""
        val imageUrl = intent.getStringExtra("imageUrl") ?: ""
        val description = intent.getStringExtra("description") ?: ""
        val condition = intent.getStringExtra("condition") ?: ""
        val location = intent.getStringExtra("location") ?: ""

        // Set the retrieved data to the views
        itemName.text = name
        itemPrice.text = price
        itemDescription.text = description
        itemCondition.text = condition
        itemLocation.text = location

        // Load the image using Glide
        Glide.with(this).load(imageUrl).into(itemImage)

        // Set up button click listeners
        addToCartButton.setOnClickListener {
            // Create a Data.Item object to add to the cart
            val item = Data.Item(
                name = name,
                price = price,
                imageUrl = imageUrl,
                description = description,
                condition = condition,
                location = location
            )
            // Add item to cart
            CartManager.addItem(item)

            // Optionally show a toast message
            Toast.makeText(this, "$name added to cart", Toast.LENGTH_SHORT).show()
        }

        // Move buyNowButton click listener outside
        buyNowButton.setOnClickListener {
            // Handle buy now action
            // e.g., proceed to checkout
        }

        // Move backBtn click listener outside
        backBtn.setOnClickListener {
            finish() // Close the current activity
        }
    }
}

package com.capstone.ecorecyc

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class AddToCartMarketplace : AppCompatActivity() {

    private lateinit var itemImage: ImageView
    private lateinit var itemName: TextView
    private lateinit var itemPrice: TextView
    private lateinit var itemDescription: TextView
    private lateinit var addToCartButton: Button
    private lateinit var buyNowButton: Button
    private lateinit var backButton: ImageButton
    private lateinit var chatButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_to_cart_marketplace)

        // Initialize views
        itemImage = findViewById(R.id.second_hand_item_pic)
        itemName = findViewById(R.id.second_hand_item_name)
        itemPrice = findViewById(R.id.second_hand_item_price)
        itemDescription = findViewById(R.id.second_hand_item_description)
        addToCartButton = findViewById(R.id.addToCartButton)
        buyNowButton = findViewById(R.id.buyNowButton)
        backButton = findViewById(R.id.backBtn)
        chatButton = findViewById(R.id.chatBtn)

        // Get data from the Intent
        val intent = intent
        val name = intent.getStringExtra("ITEM_NAME")
        val price = intent.getStringExtra("ITEM_PRICE")
        val description = intent.getStringExtra("ITEM_DESCRIPTION")
        val imageUrl = intent.getStringExtra("ITEM_IMAGE_URL") // If you're using a URL

        // Set the data to views
        itemName.text = name
        itemPrice.text = price
        itemDescription.text = description
        // Load image using your preferred image loading library (e.g., Glide or Picasso)
        // Glide.with(this).load(imageUrl).into(itemImage)

        // Set button click listeners
        addToCartButton.setOnClickListener {
            // Handle add to cart action
        }

        buyNowButton.setOnClickListener {
            // Handle buy now action
        }

        backButton.setOnClickListener {
            finish() // Go back to the previous activity
        }

        chatButton.setOnClickListener {
            // Handle chat action
        }
    }
}

package com.capstone.ecorecyc

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class ItemDetails : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_details)

        val itemName: TextView = findViewById(R.id.itemName)
        val itemPrice: TextView = findViewById(R.id.itemPrice)
        val itemDescription: TextView = findViewById(R.id.itemDescription)
        val itemCondition: TextView = findViewById(R.id.itemCondition)
        val itemLocation: TextView = findViewById(R.id.itemLocation)
        val itemImage: ImageView = findViewById(R.id.itemImage)
        val addToCartButton: Button = findViewById(R.id.addToCartButton)

        // Get the passed item details
        val name = intent.getStringExtra("name")
        val price = intent.getStringExtra("price")
        val description = intent.getStringExtra("description")
        val condition = intent.getStringExtra("condition")
        val location = intent.getStringExtra("location")
        val imageUrl = intent.getStringExtra("imageUrl")

        // Log received data for debugging
        Log.d("ItemDetails", "Name: $name, Price: $price, Description: $description, Condition: $condition, Location: $location")

        // Display the item details
        itemName.text = name ?: "No name available"
        itemPrice.text = price ?: "No price available"
        itemDescription.text = description ?: "Description"
        itemCondition.text = condition ?: "Condition"
        itemLocation.text = location ?: "Location "
        Glide.with(this).load(imageUrl).into(itemImage)

        // Handle Add to Cart button click
        addToCartButton.setOnClickListener {
            CartManager.addItemToCart(name, price, imageUrl)
            Toast.makeText(this, "Item added to cart!", Toast.LENGTH_SHORT).show()
        }
    }
}

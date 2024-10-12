package com.capstone.ecorecyc

import android.os.Bundle
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
        val itemImage: ImageView = findViewById(R.id.itemImage)
        val addToCartButton: Button = findViewById(R.id.addToCartButton)

        // Get the passed item details
        val name = intent.getStringExtra("name")
        val price = intent.getStringExtra("price")
        val imageUrl = intent.getStringExtra("imageUrl")

        // Display the item details
        itemName.text = name
        itemPrice.text = price
        Glide.with(this).load(imageUrl).into(itemImage)

        // Handle Add to Cart button click
        addToCartButton.setOnClickListener {
            CartManager.addItemToCart(name, price, imageUrl)
            Toast.makeText(this, "Item added to cart!", Toast.LENGTH_SHORT).show()
        }
    }
}

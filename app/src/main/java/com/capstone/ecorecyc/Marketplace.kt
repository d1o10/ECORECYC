package com.capstone.ecorecyc

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class Marketplace : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private val firestore = FirebaseFirestore.getInstance()
    private val itemList = mutableListOf<Data.Item>()  // Use Data.Item

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_marketplace)

        recyclerView = findViewById(R.id.recyclerView)

        // Set the layout manager to display a grid of 2 columns
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        fetchItems()
    }

    private fun fetchItems() {
        firestore.collection("items").get().addOnSuccessListener { documents ->
            for (document in documents) {
                val item = document.toObject(Data.Item::class.java)
                itemList.add(item)
            }
            recyclerView.adapter = MarketplaceAdapter(itemList)
        }.addOnFailureListener { exception ->
            Log.e("Marketplace", "Error getting documents: ", exception)
        }

        // Move this out of the failure listener block
        val sellitembutton: ImageButton = findViewById(R.id.sellItemBtn)
        sellitembutton.setOnClickListener {
            val intent = Intent(this, SellAnItem::class.java)
            intent.putExtra("USER_TYPE", "USER")
            startActivity(intent)
        }
    }
}

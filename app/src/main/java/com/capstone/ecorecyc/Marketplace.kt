package com.capstone.ecorecyc

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class Marketplace : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var searchBox: EditText
    private val firestore = FirebaseFirestore.getInstance()
    private val itemList = mutableListOf<Data.Item>()
    private lateinit var adapter: MarketplaceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_marketplace)

        recyclerView = findViewById(R.id.recyclerView)
        searchBox = findViewById(R.id.searchbox)

        // Set the layout manager to display a grid of 2 columns
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        // Initialize the adapter and set it to the RecyclerView
        adapter = MarketplaceAdapter(itemList)
        recyclerView.adapter = adapter

        // Fetch all items initially
        fetchItems()

        // Add a TextWatcher to the search box to listen for changes
        searchBox.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString().trim()
                if (query.isNotEmpty()) {
                    searchItems(query)
                } else {
                    // Fetch all items when the search box is cleared
                    fetchItems()
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    // Fetch all items from Firestore
    private fun fetchItems() {
        firestore.collection("items")
            .get()
            .addOnSuccessListener { documents ->
                itemList.clear()  // Clear the current list
                for (document in documents) {
                    val item = document.toObject(Data.Item::class.java)
                    itemList.add(item)
                }
                adapter.notifyDataSetChanged()  // Notify the adapter of the data change
            }
            .addOnFailureListener { exception ->
                Log.e("Marketplace", "Error getting documents: ", exception)
            }
    }

    // Search for items by name in Firestore
    private fun searchItems(query: String) {
        firestore.collection("items")
            .whereGreaterThanOrEqualTo("name", query)
            .whereLessThanOrEqualTo("name", query + '\uf8ff') // Unicode trick for prefix matching
            .get()
            .addOnSuccessListener { documents ->
                itemList.clear()  // Clear the current list
                for (document in documents) {
                    val item = document.toObject(Data.Item::class.java)
                    itemList.add(item)
                }
                adapter.notifyDataSetChanged()  // Notify the adapter of the data change
            }
            .addOnFailureListener { exception ->
                Log.e("Marketplace", "Error searching documents: ", exception)
            }
    }
}

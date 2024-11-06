package com.capstone.ecorecyc

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Locale

class Marketplace : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var searchBox: EditText
    private lateinit var sellItemBtn: ImageButton
    private lateinit var locationTextView: TextView
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val firestore = FirebaseFirestore.getInstance()
    private val itemList = mutableListOf<Data.Item>()
    private lateinit var adapter: MarketplaceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_marketplace)

        recyclerView = findViewById(R.id.recyclerView)
        searchBox = findViewById(R.id.searchbox)
        sellItemBtn = findViewById(R.id.sellItemBtn)
        locationTextView = findViewById(R.id.locationTextView)

        recyclerView.layoutManager = GridLayoutManager(this, 2)
        adapter = MarketplaceAdapter(itemList)
        recyclerView.adapter = adapter

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        checkLocationPermission()

        fetchItems()

        searchBox.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString().trim()
                if (query.isNotEmpty()) {
                    searchItems(query)
                } else {
                    fetchItems()
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        sellItemBtn.setOnClickListener {
            val intent = Intent(this, SellAnItem::class.java)
            startActivity(intent)
        }
    }

    private fun checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        } else {
            fetchLocation()
        }
    }

    private fun fetchLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                location?.let {
                    updateLocationUI(it)
                }
            }
        }
    }

    private fun updateLocationUI(location: Location) {
        val geocoder = Geocoder(this, Locale.getDefault())
        val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
        if (!addresses.isNullOrEmpty()) {
            val address = addresses[0]
            val locationName = address.locality ?: address.subAdminArea ?: "Unknown Location"
            locationTextView.text = locationName
        } else {
            locationTextView.text = "Location not found"
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            fetchLocation()
        }
    }

    private fun fetchItems() {
        firestore.collection("items")
            .get()
            .addOnSuccessListener { documents ->
                itemList.clear()
                for (document in documents) {
                    val item = document.toObject(Data.Item::class.java)
                    itemList.add(item)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.e("Marketplace", "Error getting documents: ", exception)
            }
    }

    private fun searchItems(query: String) {
        firestore.collection("items")
            .whereGreaterThanOrEqualTo("name", query)
            .whereLessThanOrEqualTo("name", query + '\uf8ff')
            .get()
            .addOnSuccessListener { documents ->
                itemList.clear()
                for (document in documents) {
                    val item = document.toObject(Data.Item::class.java)
                    itemList.add(item)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.e("Marketplace", "Error searching documents: ", exception)
            }
    }
}
package com.capstone.ecorecyc

class Data {

    data class Item(
        val name: String = "",
        val price: String = "",
        val imageUrl: String = "",
        val description: String = "", // New field for item description
        val condition: String = "",    // New field for item condition
        val location: String = ""       // New field for item location
    )
}

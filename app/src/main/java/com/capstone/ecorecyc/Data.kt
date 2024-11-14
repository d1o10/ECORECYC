package com.capstone.ecorecyc

class Data {

    data class Item(
        val name: String = "",
        val price: String = "",
        val imageUrl: String = "",
        val description: String = "",
        val condition: String = "",
        val location: String = "",
        val username: String = "" // New field for the username
    )
}

package com.capstone.ecorecyc

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Marketplace : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_marketplace)


        val sellbtn: ImageButton = findViewById(R.id.sellItemBtn)
        sellbtn.setOnClickListener {
            val intent = Intent(this, SellAnItem::class.java)
            intent.putExtra("USER_TYPE", "USER")
            startActivity(intent)
        }

    }

}
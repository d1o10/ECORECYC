package com.capstone.ecorecyc

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Dashboard : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dashboard)

        val market: ImageButton = findViewById(R.id.marketplacebutton)
        market.setOnClickListener {
            val intent = Intent(this, Marketplace::class.java)
            intent.putExtra("USER_TYPE", "USER")
            startActivity(intent)

            val recylingbtn: ImageButton = findViewById(R.id.recylinghubbutton)
            recylingbtn.setOnClickListener {
                val intent = Intent(this, RecyclingHub::class.java)
                intent.putExtra("USER_TYPE", "USER")
                startActivity(intent)

                val cleanbtn: ImageButton = findViewById(R.id.cleanupbtn)
                cleanbtn.setOnClickListener {
                    val intent = Intent(this, CleanupEvents::class.java)
                    intent.putExtra("USER_TYPE", "USER")
                    startActivity(intent)
                }
            }
        }
    }
}
package com.capstone.ecorecyc

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class CleanupEvents : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cleanup_events)

        val organizebtn: Button = findViewById(R.id.organize_cleanup_btn)
        organizebtn.setOnClickListener {
            val intent = Intent(this, OrganizeCleanupEvent::class.java)
            intent.putExtra("USER_TYPE", "USER")
            startActivity(intent)
        }

    }
}
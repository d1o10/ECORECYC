package com.capstone.ecorecyc

import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Settings : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)

        val editprof: ImageButton = findViewById(R.id.ediprofilebutton)
        editprof.setOnClickListener {
            val intent = Intent(this, EditProfile::class.java)
            intent.putExtra("USER_TYPE", "USER")
            startActivity(intent)
        }

    }
}
package com.capstone.ecorecyc

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class UserProfile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_user_profile)


        val backbtnprof: ImageButton = findViewById(R.id.backbtnprofile)
        backbtnprof.setOnClickListener {
            val intent = Intent(this, Dashboard::class.java)
            intent.putExtra("USER_TYPE", "USER")
            startActivity(intent)
        }
        val logout: ImageButton = findViewById(R.id.logout_btn)
        logout  .setOnClickListener {
            val intent = Intent(this, Login::class.java)
            intent.putExtra("USER_TYPE", "USER")
            startActivity(intent)
        }
    }
}
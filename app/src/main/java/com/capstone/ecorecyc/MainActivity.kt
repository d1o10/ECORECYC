package com.capstone.ecorecyc

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.capstone.ecorecyc.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.EcorecycLogo)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Delay for 3 seconds (3000 milliseconds) and then start LoginSignupActivity
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, ChooseRole::class.java)
            startActivity(intent)
            finish()  // Close the MainActivity
        }, 3000)  // 3000 milliseconds delay

    }
}
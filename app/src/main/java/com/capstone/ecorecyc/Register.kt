package com.capstone.ecorecyc

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Register : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        val login2btn: Button = findViewById(R.id.loginregister)
        login2btn.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            intent.putExtra("USER_TYPE", "USER")
            startActivity(intent)
        }
    }
}
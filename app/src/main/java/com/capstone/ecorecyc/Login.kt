package com.capstone.ecorecyc

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)


        val loginBtn: Button = findViewById(R.id.btn_login)
        loginBtn.setOnClickListener {
            val intent = Intent(this, Dashboard::class.java)
            intent.putExtra("USER_TYPE", "USER")
            startActivity(intent)
        }

        val signuptn: Button = findViewById(R.id.Signup)
        signuptn.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            intent.putExtra("USER_TYPE", "USER")
            startActivity(intent)
        }
        }
    }

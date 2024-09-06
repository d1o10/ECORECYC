package com.capstone.ecorecyc

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ChooseRole : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_choose_role)



        val UserBtn: Button = findViewById(R.id.radio_user)
        UserBtn.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            intent.putExtra("USER_TYPE", "USER")
            startActivity(intent)
        }


        val JunkOwnerBtn: Button = findViewById(R.id.radio_junkshop_owner)
        JunkOwnerBtn.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            intent.putExtra("USER_TYPE", "USER")
            startActivity(intent)
        }

    }
}
package com.capstone.ecorecyc

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout

class Chat : AppCompatActivity() {

    private lateinit var mToolbar: Toolbar
    private lateinit var chatViewPager: ViewPager
    private lateinit var chatTablayout: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        // Initialize the toolbar
        mToolbar = findViewById(R.id.chat_page_toolbar)
        setSupportActionBar(mToolbar)
        supportActionBar?.title = "Chat"
    }
}

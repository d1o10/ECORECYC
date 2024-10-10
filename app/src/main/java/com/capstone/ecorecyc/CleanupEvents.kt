package com.capstone.ecorecyc

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class CleanupEvents : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var eventAdapter: EventAdapter
    private lateinit var eventList: MutableList<Event>
    private val firestore = FirebaseFirestore.getInstance()
    private var eventListener: ListenerRegistration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cleanup_events)

        val organizeButton: Button = findViewById(R.id.organize_cleanup_btn)
        organizeButton.setOnClickListener {
            val intent = Intent(this, OrganizeCleanupEvent::class.java)
            startActivity(intent)
        }

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        eventList = mutableListOf()
        eventAdapter = EventAdapter(eventList)
        recyclerView.adapter = eventAdapter

        fetchEventsFromFirestore()
    }

    override fun onResume() {
        super.onResume()
        fetchEventsFromFirestore()
    }

    override fun onDestroy() {
        super.onDestroy()
        eventListener?.remove()
    }

    private fun fetchEventsFromFirestore() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        val userId = currentUser.uid
        eventListener = firestore.collection("users").document(userId)
            .collection("cleanup_events")
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Toast.makeText(this, "Failed to load events", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                eventList.clear()
                for (doc in snapshots!!) {
                    val event = doc.toObject(Event::class.java)
                    eventList.add(event)
                }
                eventAdapter.notifyDataSetChanged()
            }
    }
}
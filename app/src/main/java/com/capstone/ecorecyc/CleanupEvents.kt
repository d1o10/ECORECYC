package com.capstone.ecorecyc

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
        eventListener = firestore.collection("cleanup_events")
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    return@addSnapshotListener
                }

                if (snapshots != null && !snapshots.isEmpty) {
                    eventList.clear()
                    for (doc in snapshots.documents) {
                        val event = doc.toObject(Event::class.java)
                        if (event != null) {
                            eventList.add(event)
                        }
                    }
                    eventAdapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(this, "No events available", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
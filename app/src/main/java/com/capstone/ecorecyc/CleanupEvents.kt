package com.capstone.ecorecyc

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CleanupEvents : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var eventAdapter: EventAdapter
    private lateinit var eventList: MutableList<Event>

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

        // Fetch events from Firebase when the activity starts
        fetchEventsFromFirebase()
    }

    override fun onResume() {
        super.onResume()
        // Fetch events again when the activity resumes
        fetchEventsFromFirebase()
    }

    private fun fetchEventsFromFirebase() {
        val databaseRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("cleanup_events")

        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                eventList.clear() // Clear existing list before adding new data

                for (eventSnapshot in dataSnapshot.children) {
                    val event = eventSnapshot.getValue(Event::class.java)
                    if (event != null) {
                        eventList.add(event)
                    }
                }

                eventAdapter.notifyDataSetChanged() // Notify adapter of data change
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@CleanupEvents, "Failed to load events", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

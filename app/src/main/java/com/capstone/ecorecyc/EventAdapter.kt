package com.capstone.ecorecyc

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class EventAdapter(private val eventList: List<Event>) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = eventList[position]
        holder.cleanupName.text = event.cleanupName
        holder.location.text = event.location
        holder.description.text = event.description
        holder.date.text = event.date
        holder.time.text = event.time // Bind the time

        // Load the event image using Glide
        Glide.with(holder.itemView.context)
            .load(event.imageUrl)
            .into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return eventList.size
    }

    class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cleanupName: TextView = itemView.findViewById(R.id.cleanup_name)
        val location: TextView = itemView.findViewById(R.id.location)
        val description: TextView = itemView.findViewById(R.id.description)
        val date: TextView = itemView.findViewById(R.id.date)
        val time: TextView = itemView.findViewById(R.id.time) // Reference the new TextView for time
        val imageView: ImageView = itemView.findViewById(R.id.event_image)
    }
}
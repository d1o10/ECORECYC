package com.capstone.ecorecyc

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MarketplaceAdapter(private var itemList: List<Data.Item>) : RecyclerView.Adapter<MarketplaceAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemImage: ImageView = view.findViewById(R.id.itemImage)
        val itemName: TextView = view.findViewById(R.id.itemName)
        val itemPrice: TextView = view.findViewById(R.id.itemPrice)
        val itemUsername: TextView = view.findViewById(R.id.byUser) // Display username here
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_marketplace, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        holder.itemName.text = item.name
        holder.itemPrice.text = item.price
        holder.itemUsername.text = "by ${item.username}" // Set the username

        // Load the image using Glide
        Glide.with(holder.itemView.context).load(item.imageUrl).into(holder.itemImage)

        // Set the onClickListener to open the ItemDetails activity
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, ItemDetails::class.java).apply {
                putExtra("name", item.name)
                putExtra("price", item.price)
                putExtra("imageUrl", item.imageUrl)
                putExtra("description", item.description)  // Pass description
                putExtra("condition", item.condition)      // Pass condition
                putExtra("location", item.location)        // Pass location
                putExtra("username", item.username)        // Pass username
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = itemList.size

    // Function to update the items list dynamically
    fun updateItems(newItems: List<Data.Item>) {
        itemList = newItems
        notifyDataSetChanged() // Notify adapter about the updated list
    }
}

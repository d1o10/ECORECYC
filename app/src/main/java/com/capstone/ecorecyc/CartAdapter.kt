package com.capstone.ecorecyc

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class CartAdapter(
    private val cartItems: MutableList<Data.Item>,
    private val onDeleteClick: (Data.Item) -> Unit
) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemImage: ImageView = view.findViewById(R.id.itemImage)
        val itemName: TextView = view.findViewById(R.id.itemName)
        val itemPrice: TextView = view.findViewById(R.id.itemPrice)
        val deleteButton: ImageButton = view.findViewById(R.id.deleteItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = cartItems[position]
        holder.itemName.text = item.name
        holder.itemPrice.text = "$${item.price}"
        Glide.with(holder.itemView.context).load(item.imageUrl).into(holder.itemImage)

        holder.deleteButton.setOnClickListener {
            onDeleteClick(item) // Handle delete action
        }
    }

    override fun getItemCount() = cartItems.size
}

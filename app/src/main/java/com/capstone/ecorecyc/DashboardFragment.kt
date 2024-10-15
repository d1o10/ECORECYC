package com.capstone.ecorecyc

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment

class DashboardFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        // Find and set up your buttons
        val market: ImageButton = view.findViewById(R.id.marketplacebutton)
        market.setOnClickListener {
            val intent = Intent(activity, Marketplace::class.java)
            intent.putExtra("USER_TYPE", "USER")
            startActivity(intent)  // This works just like in an activity
        }

        val recyclingbtn: ImageButton = view.findViewById(R.id.recylinghubbutton)
        recyclingbtn.setOnClickListener {
            val intent = Intent(activity, RecyclingHub::class.java)
            intent.putExtra("USER_TYPE", "USER")
            startActivity(intent)
        }

        val cleanbtn: ImageButton = view.findViewById(R.id.cleanupbtn)
        cleanbtn.setOnClickListener {
            val intent = Intent(activity, CleanupEvents::class.java)
            intent.putExtra("USER_TYPE", "USER")
            startActivity(intent)
        }

        val profbtn: ImageButton = view.findViewById(R.id.profilebtn)
        profbtn.setOnClickListener {
            val intent = Intent(activity, UserProfileFragment::class.java)
            intent.putExtra("USER_TYPE", "USER")
            startActivity(intent)
        }

        // Add the chat button functionality
        val chatBtn: ImageButton = view.findViewById(R.id.chatBtn)
        chatBtn.setOnClickListener {
            val intent = Intent(activity, Chat::class.java)
            startActivity(intent)
        }

        return view
    }
}

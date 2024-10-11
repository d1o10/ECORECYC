package com.capstone.ecorecyc

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment

class UserProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_user_profile, container, false)

        // Back button to navigate to DashboardFragment
        val backBtn: ImageButton = view.findViewById(R.id.backbtnprofile)
        backBtn.setOnClickListener {
            val intent = Intent(activity, DashboardFragment::class.java)
            intent.putExtra("USER_TYPE", "USER")
            startActivity(intent)
        }

        // Logout button to navigate to Login
        val logoutBtn: ImageButton = view.findViewById(R.id.logout_btn)
        logoutBtn.setOnClickListener {
            val intent = Intent(activity, Login::class.java)
            intent.putExtra("USER_TYPE", "USER")
            startActivity(intent)
        }

        return view
    }
}

package com.capstone.ecorecyc

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class MyOrdersPagerAdapter(
    activity: AppCompatActivity, // Activity context
    private val fragments: List<Fragment> // List of fragments to display in the ViewPager
) : FragmentStateAdapter(activity) {  // Inherit from FragmentStateAdapter

    // Return the number of fragments
    override fun getItemCount(): Int {
        return fragments.size
    }

    // Create and return the fragment at the given position
    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}

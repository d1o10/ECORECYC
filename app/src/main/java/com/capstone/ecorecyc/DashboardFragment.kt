package com.capstone.ecorecyc

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.Locale

class DashboardFragment : Fragment() {

    private lateinit var sharedPreferences: SharedPreferences
    private val PREFS_NAME = "LoginPrefs"
    private lateinit var greetingTextView: TextView
    private lateinit var locationTextView: TextView
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        sharedPreferences = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        greetingTextView = view.findViewById(R.id.hiusername)
        locationTextView = view.findViewById(R.id.locationTextView)

        updateGreeting()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        checkLocationPermission()

        setupButtons(view)

        return view
    }

    private fun setupButtons(view: View) {
        val market: ImageButton = view.findViewById(R.id.marketplacebutton)
        market.setOnClickListener {
            val intent = Intent(activity, Marketplace::class.java)
            intent.putExtra("USER_TYPE", "USER")
            startActivity(intent)
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

        val chatBtn: ImageButton = view.findViewById(R.id.notificationBtn)
        chatBtn.setOnClickListener {
            val intent = Intent(activity, Chat::class.java)
            startActivity(intent)
        }
    }

    private fun checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        } else {
            fetchLocation()
        }
    }

    private fun fetchLocation() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                location?.let {
                    updateLocationUI(it)
                }
            }
        }
    }

    private fun updateLocationUI(location: Location) {
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
        if (!addresses.isNullOrEmpty()) {
            val address = addresses[0]
            val locationName = address.locality ?: address.subAdminArea ?: "Unknown Location"
            locationTextView.text = locationName
        } else {
            locationTextView.text = "Location not found"
        }
    }

    override fun onResume() {
        super.onResume()
        updateGreeting()
        fetchLocation()
    }

    private fun updateGreeting() {
        val username = sharedPreferences.getString("username", "User")
        greetingTextView.text = "$username!"
    }
}
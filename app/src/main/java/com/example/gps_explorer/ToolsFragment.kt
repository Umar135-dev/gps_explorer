package com.example.gps_explorer

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment

/** Tools tab content ("More Tools"). The bottom navigation lives in MainActivity. */
class ToolsFragment : Fragment(R.layout.fragment_tools) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<View>(R.id.btnSettings).setOnClickListener {
            startActivity(Intent(requireContext(), SettingsActivity::class.java))
        }

        // Street View
        view.findViewById<View>(R.id.btnExploreStreets).setOnClickListener {
            // TODO: open Street View
            comingSoon()
        }
        view.findViewById<View>(R.id.btnExpandStreet).setOnClickListener {
            // TODO: open Street View full screen
            comingSoon()
        }
        view.findViewById<View>(R.id.cardStreetView).setOnClickListener {
            comingSoon()
        }

        // Famous Places
        view.findViewById<View>(R.id.tvSeeAll).setOnClickListener {
            // TODO: open the full list of famous places
            comingSoon()
        }
        view.findViewById<View>(R.id.placeNature).setOnClickListener { openCategory("nature") }
        view.findViewById<View>(R.id.placeHistoric).setOnClickListener { openCategory("historic") }
        view.findViewById<View>(R.id.placeCity).setOnClickListener { openCategory("city") }
        view.findViewById<View>(R.id.placeBeaches).setOnClickListener { openCategory("beaches") }

        // Travel tools
        view.findViewById<View>(R.id.cardCamera).setOnClickListener { openGpsCamera() }
        view.findViewById<View>(R.id.btnCameraGo).setOnClickListener { openGpsCamera() }
        view.findViewById<View>(R.id.cardCompass).setOnClickListener {
            // TODO: open the Compass screen
            comingSoon()
        }
        view.findViewById<View>(R.id.cardParking).setOnClickListener {
            // TODO: open the Saved Parking screen
            comingSoon()
        }
    }

    private fun openCategory(category: String) {
        // TODO: open the places list filtered by [category]
        comingSoon()
    }

    private fun openGpsCamera() {
        // TODO: open the GPS Camera screen
        comingSoon()
    }

    private fun comingSoon() {
        Toast.makeText(requireContext(), "Coming soon", Toast.LENGTH_SHORT).show()
    }
}

package com.example.gps_explorer

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment


class HomeFragment : Fragment(R.layout.fragment_home) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Header
        view.findViewById<View>(R.id.btnLocation).setOnClickListener {
            // TODO: open a location picker
            comingSoon()
        }
        view.findViewById<View>(R.id.btnSettings).setOnClickListener {
            startActivity(Intent(requireContext(), SettingsActivity::class.java))
        }

        // AI search bar
        view.findViewById<View>(R.id.searchBar).setOnClickListener { openAi() }
        view.findViewById<View>(R.id.btnAskGo).setOnClickListener { openAi() }

        // Live Earth Camera card
        view.findViewById<View>(R.id.btnExploreEarth).setOnClickListener {
            // TODO: open the Live Earth screen
            comingSoon()
        }
        view.findViewById<View>(R.id.btnExpand).setOnClickListener {
            // TODO: open the Live Earth screen in full screen
            comingSoon()
        }

        // Route Finder card
        view.findViewById<View>(R.id.btnFindRoute).setOnClickListener {
            // TODO: open the Route Finder screen
            comingSoon()
        }
    }

    private fun openAi() {
        // TODO: open the AI assistant screen
        comingSoon()
    }

    private fun comingSoon() {
        Toast.makeText(requireContext(), "Coming soon", Toast.LENGTH_SHORT).show()
    }
}

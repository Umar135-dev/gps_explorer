package com.example.gps_explorer

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment


class PlannerFragment : Fragment(R.layout.fragment_planner) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Header
        view.findViewById<View>(R.id.btnMyTrips).setOnClickListener {
            // TODO: open the list of saved trips
            comingSoon()
        }
        view.findViewById<View>(R.id.btnSettings).setOnClickListener {
            startActivity(Intent(requireContext(), SettingsActivity::class.java))
        }

        // Main action
        view.findViewById<View>(R.id.btnPlanTrip).setOnClickListener {
            // TODO: open the "plan my trip" flow (destination, dates, interests...)
            comingSoon()
        }
    }

    private fun comingSoon() {
        Toast.makeText(requireContext(), "Coming soon", Toast.LENGTH_SHORT).show()
    }
}

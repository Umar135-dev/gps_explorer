package com.example.gps_explorer

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class OnboardingFeaturesFragment : Fragment(R.layout.fragment_onboarding_features) {

    private val items = listOf(
        OptionItem("route", "Route Finder", R.drawable.ic_route, true),
        OptionItem("nearby", "Nearby Places", R.drawable.ic_nearby),
        OptionItem("earth", "Earth Map", R.drawable.ic_earth, true),
        OptionItem("famous", "Famous Places", R.drawable.ic_star),
        OptionItem("street", "Street View", R.drawable.ic_street_view),
        OptionItem("parking", "Parking Reminder", R.drawable.ic_parking),
        OptionItem("camera", "GPS Camera", R.drawable.ic_camera),
        OptionItem("compass", "Compass", R.drawable.ic_compass, true)
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnContinue = view.findViewById<View>(R.id.btnContinue)
        val rvOptions = view.findViewById<RecyclerView>(R.id.rvOptions)

        rvOptions.layoutManager = LinearLayoutManager(requireContext())
        rvOptions.adapter = OptionsAdapter(items) {
            updateButton(btnContinue)
        }

        updateButton(btnContinue)

        btnContinue.setOnClickListener {
            val chosen = items
                .filter { it.selected }
                .map { it.id }
                .toSet()

            if (chosen.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Choose at least one option",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            requireContext()
                .getSharedPreferences("onboarding", Context.MODE_PRIVATE)
                .edit { putStringSet("features", chosen) }

            // Swap only the content; the progress bar stays in OnboardingActivity
            (requireActivity() as OnboardingActivity)
                .goTo(OnboardingFamiliarityFragment())
        }
    }

    private fun updateButton(btn: View) {
        val enabled = items.any { it.selected }
        btn.isEnabled = enabled
        btn.alpha = if (enabled) 1f else 0.5f
    }
}
package com.example.gps_explorer

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment

class OnboardingSourceFragment : Fragment(R.layout.fragment_onboarding_source) {

    private data class Source(val id: String, val label: String, val iconRes: Int)

    private val sources = listOf(
        Source("google_play", "Google Play", R.drawable.ic_google_play),
        Source("instagram", "Instagram", R.drawable.ic_instagram),
        Source("tiktok", "TikTok", R.drawable.ic_tiktok),
        Source("youtube", "YouTube", R.drawable.ic_youtube),
        Source("friends_family", "Friends or Family", R.drawable.ic_friends),
        Source("other", "Other", R.drawable.ic_other_dots),
    )

    private var selected = -1   // nothing selected at the start
    private val rows = mutableListOf<View>()
    private lateinit var btnContinue: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        selected = savedInstanceState?.getInt("selected", -1) ?: -1
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("selected", selected)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rows.clear()
        val container = view.findViewById<LinearLayout>(R.id.optionsContainer)
        val inflater = LayoutInflater.from(requireContext())

        sources.forEachIndexed { i, s ->
            val row = inflater.inflate(R.layout.item_source, container, false)
            row.findViewById<ImageView>(R.id.ivSource).setImageResource(s.iconRes)
            row.findViewById<TextView>(R.id.tvSource).text = s.label
            row.setOnClickListener {
                selected = i
                renderRows()
            }
            container.addView(row)
            rows.add(row)
        }

        btnContinue = view.findViewById(R.id.btnContinue)
        btnContinue.setOnClickListener {
            if (selected < 0) {
                Toast.makeText(
                    requireContext(),
                    "Please choose one option",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            requireContext()
                .getSharedPreferences("onboarding", Context.MODE_PRIVATE)
                .edit()
                .putString("source", sources[selected].id)
                .apply()

            (requireActivity() as OnboardingActivity)
                .goTo(OnboardingDoneFragment())
        }

        renderRows()
    }

    private fun renderRows() {
        rows.forEachIndexed { i, row ->
            val on = i == selected
            row.setBackgroundResource(
                if (on) R.drawable.bg_option_selected else R.drawable.bg_option_default
            )
            row.findViewById<ImageView>(R.id.ivRadio).setImageResource(
                if (on) R.drawable.radio_on else R.drawable.radio_off
            )
        }
        btnContinue.alpha = if (selected >= 0) 1f else 0.5f
    }
}
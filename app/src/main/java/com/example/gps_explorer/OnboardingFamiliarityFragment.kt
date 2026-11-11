package com.example.gps_explorer

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment

class OnboardingFamiliarityFragment : Fragment(R.layout.fragment_onboarding_familiarity) {

    private data class Choice(val id: String, val label: String, val iconRes: Int)

    private val choices = listOf(
        Choice("new", "I'm new to GPS apps", R.drawable.ic_signal_low),
        Choice("sometimes", "I use them sometimes", R.drawable.ic_signal_mid),
        Choice("often", "I use them often", R.drawable.ic_signal_high),
    )

    private var selected = 1   // "I use them sometimes" is preselected
    private lateinit var rows: List<View>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        selected = savedInstanceState?.getInt("selected", 1) ?: 1
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("selected", selected)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rowIds: List<Int> = listOf(R.id.optNew, R.id.optSometimes, R.id.optOften)
        rows = rowIds.map { id: Int -> view.findViewById<View>(id) }

        rows.forEachIndexed { i, row ->
            row.findViewById<ImageView>(R.id.ivSignal).setImageResource(choices[i].iconRes)
            row.findViewById<TextView>(R.id.tvChoice).text = choices[i].label
            row.setOnClickListener {
                selected = i
                renderRows()
            }
        }
        renderRows()

        view.findViewById<View>(R.id.btnContinue).setOnClickListener {
            requireContext()
                .getSharedPreferences("onboarding", Context.MODE_PRIVATE)
                .edit()
                .putString("familiarity", choices[selected].id)
                .apply()

            (requireActivity() as OnboardingActivity)
                .goTo(OnboardingSourceFragment())
        }
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
    }
}
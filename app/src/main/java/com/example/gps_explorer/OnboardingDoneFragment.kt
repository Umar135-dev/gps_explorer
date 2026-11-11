package com.example.gps_explorer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.OvershootInterpolator
import android.widget.ImageView
import androidx.fragment.app.Fragment

class OnboardingDoneFragment : Fragment(R.layout.fragment_onboarding_done) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        popInPin(view.findViewById(R.id.ivPin))

        view.findViewById<View>(R.id.btnGetStarted).setOnClickListener {
            val ctx = requireContext()

            ctx.getSharedPreferences("onboarding", Context.MODE_PRIVATE)
                .edit()
                .putBoolean("completed", true)
                .apply()

            startActivity(
                Intent(ctx, LanguageActivity::class.java).apply {
                    putExtra("from_onboarding", true)
                }
            )
            requireActivity().finish()
        }
    }

    /** The pin pops in with a small bounce. */
    private fun popInPin(pin: ImageView) {
        pin.scaleX = 0f
        pin.scaleY = 0f
        pin.animate()
            .scaleX(1f).scaleY(1f)
            .setDuration(600)
            .setInterpolator(OvershootInterpolator(1.6f))
            .start()
    }
}
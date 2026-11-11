package com.example.gps_explorer

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment

class OnboardingActivity : AppCompatActivity() {

    companion object {
        const val TOTAL_STEPS = 4
    }

    private lateinit var progressContainer: LinearLayout
    private var currentStep = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_onboarding)
        WindowCompat.getInsetsController(window, window.decorView)
            .isAppearanceLightStatusBars = true

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.root)) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        progressContainer = findViewById(R.id.progressContainer)
        buildProgress()

        supportFragmentManager.addOnBackStackChangedListener {
            updateProgress(supportFragmentManager.backStackEntryCount + 1)
        }

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragmentContainer, OnboardingFeaturesFragment())
                .commit()
        }
        updateProgress(supportFragmentManager.backStackEntryCount + 1)
    }

    /** Called by the fragments when Continue is pressed. */
    fun goTo(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .setReorderingAllowed(true)
            .setCustomAnimations(
                android.R.anim.fade_in,
                android.R.anim.fade_out,
                android.R.anim.fade_in,
                android.R.anim.fade_out
            )
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun buildProgress() {
        progressContainer.removeAllViews()
        val d = resources.displayMetrics.density
        for (i in 1..TOTAL_STEPS) {
            val seg = View(this)
            val lp = LinearLayout.LayoutParams((36 * d).toInt(), (4 * d).toInt())
            if (i > 1) lp.marginStart = (8 * d).toInt()
            seg.layoutParams = lp
            seg.setBackgroundResource(R.drawable.seg_inactive)
            progressContainer.addView(seg)
        }
    }

    private fun updateProgress(step: Int) {
        val movingForward = step > currentStep && currentStep != 0

        for (i in 0 until progressContainer.childCount) {
            val seg = progressContainer.getChildAt(i)
            val active = i + 1 <= step
            seg.setBackgroundResource(if (active) R.drawable.seg_active else R.drawable.seg_inactive)

            // Newly filled segment grows in from the left
            if (movingForward && i + 1 == step) {
                seg.pivotX = 0f
                seg.scaleX = 0.2f
                seg.animate().scaleX(1f).setDuration(300).start()
            } else {
                seg.scaleX = 1f
            }
        }
        currentStep = step
    }
}
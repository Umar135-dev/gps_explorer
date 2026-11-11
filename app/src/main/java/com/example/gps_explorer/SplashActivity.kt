package com.example.gps_explorer

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat

class SplashActivity : AppCompatActivity() {

    private val handler = Handler(Looper.getMainLooper())
    private lateinit var dots: List<ImageView>
    private var activeDot = 2          // the third dot starts active, like the design

    private val dotRunnable = object : Runnable {
        override fun run() {
            activeDot = (activeDot + 1) % dots.size
            dots.forEachIndexed { i, dot ->
                dot.setImageResource(if (i == activeDot) R.drawable.dot_active else R.drawable.dot_inactive)
            }
            handler.postDelayed(this, 450)
        }
    }

    private val goNext = Runnable {
        val done = !BuildConfig.DEBUG && getSharedPreferences("onboarding", MODE_PRIVATE)
            .getBoolean("completed", false)

        val next: Class<out AppCompatActivity> =
            if (done) MainActivity::class.java else OnboardingActivity::class.java

        startActivity(Intent(this, next))
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)

        // Dark status bar icons on the light background
        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = true

        dots = listOf(
            findViewById(R.id.dot1), findViewById(R.id.dot2), findViewById(R.id.dot3),
            findViewById(R.id.dot4), findViewById(R.id.dot5)
        )

        animateEntrance()
        handler.postDelayed(dotRunnable, 450)
        handler.postDelayed(goNext, 3000)
    }

    private fun animateEntrance() {
        // Globe gently floats up and down
        ObjectAnimator.ofFloat(findViewById<ImageView>(R.id.ivGlobe), "translationY", 0f, -16f).apply {
            duration = 1400
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
            interpolator = AccelerateDecelerateInterpolator()
            start()
        }
        // Title and tagline fade in
        listOf(R.id.tvTitle, R.id.tvTagline, R.id.tvFooter).forEachIndexed { i, id ->
            findViewById<TextView>(id).apply {
                alpha = 0f
                animate().alpha(1f).setStartDelay(200L * i).setDuration(500).start()
            }
        }
    }

    override fun onDestroy() {
        handler.removeCallbacksAndMessages(null)
        super.onDestroy()
    }
}
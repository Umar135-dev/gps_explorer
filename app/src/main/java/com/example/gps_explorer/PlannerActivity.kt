package com.example.gps_explorer

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat

class PlannerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_planner)
        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = true

        // Keep content clear of the status bar and navigation bar
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.root)) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        // Header
        findViewById<View>(R.id.btnMyTrips).setOnClickListener {
            // TODO: open the list of saved trips
            comingSoon()
        }
        findViewById<View>(R.id.btnSettings).setOnClickListener {
            startActivity(Intent(this, LanguageActivity::class.java))
        }

        // Main action
        findViewById<View>(R.id.btnPlanTrip).setOnClickListener {
            // TODO: open the "plan my trip" flow (destination, dates, interests...)
            comingSoon()
        }

        // Bottom navigation (Planner is the current tab)
        findViewById<View>(R.id.navHome).setOnClickListener {
            // Go back to Home without stacking a second copy of it
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }
        findViewById<View>(R.id.navPlanner).setOnClickListener { /* already on Planner */ }
        findViewById<View>(R.id.navTools).setOnClickListener {
            // TODO: open the Tools screen
            comingSoon()
        }
        findViewById<View>(R.id.navPro).setOnClickListener {
            startActivity(Intent(this, ProActivity::class.java))
        }
        findViewById<View>(R.id.fabAi).setOnClickListener {
            // TODO: open the AI assistant
            comingSoon()
        }
    }

    private fun comingSoon() {
        Toast.makeText(this, "Coming soon", Toast.LENGTH_SHORT).show()
    }
}

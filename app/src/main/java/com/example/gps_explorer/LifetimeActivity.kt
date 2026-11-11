package com.example.gps_explorer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat

class LifetimeActivity : AppCompatActivity() {

    private val features = listOf(
        "Explore with premium GPS tools",
        "Enjoy an ad-free experience",
        "One payment, no subscription",
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_lifetime)
        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = true

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.root)) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        // Feature rows
        val container = findViewById<LinearLayout>(R.id.featuresContainer)
        features.forEach { text ->
            val row = LayoutInflater.from(this).inflate(R.layout.item_feature, container, false)
            row.findViewById<TextView>(R.id.tvFeature).apply {
                this.text = text
                setTextColor(ContextCompat.getColor(context, R.color.screen_text_muted))
            }
            container.addView(row)
        }

        // Close (X) and system Back both open the "Explore Like a Pro" screen
        findViewById<View>(R.id.btnClose).setOnClickListener { openProScreen() }
        onBackPressedDispatcher.addCallback(this) { openProScreen() }

        findViewById<View>(R.id.btnClaim).setOnClickListener {
            getSharedPreferences("settings", Context.MODE_PRIVATE)
                .edit().putBoolean("lifetime_claimed", true).apply()

            // TODO: start the Google Play Billing flow for the $59.99 one-time product
            Toast.makeText(this, "Billing not connected yet", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openProScreen() {
        startActivity(Intent(this, ProActivity::class.java))
        finish() // so Back from the Pro screen doesn't return to Lifetime
    }
}
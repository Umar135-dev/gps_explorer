package com.example.gps_explorer

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.materialswitch.MaterialSwitch

class PremiumActivity : AppCompatActivity() {

    private val features = listOf(
        "Explore without ads",
        "Plan your trips with AI",
        "Get more AI assistant access",
        "Unlock premium GPS tools",
    )

    // Class-level property: this is where the line belongs
    private val billing: BillingService by lazy { FakeBillingManager(this) } // swap later

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_premium)
        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = true

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.root)) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        // Feature list
        val container = findViewById<LinearLayout>(R.id.featuresContainer)
        features.forEach { text ->
            val row = LayoutInflater.from(this).inflate(R.layout.item_feature, container, false)
            row.findViewById<TextView>(R.id.tvFeature).text = text
            container.addView(row)
        }

        // Billing
        billing.listener = object : BillingListener {
            override fun onPremiumChanged(isPremium: Boolean) {
                if (isPremium) goHome()
            }
            override fun onMessage(message: String) {
                Toast.makeText(this@PremiumActivity, message, Toast.LENGTH_SHORT).show()
            }
        }
        billing.connect { price ->
            findViewById<TextView>(R.id.tvPrice).text = "3 days free, then $price/year"
        }

        findViewById<View>(R.id.btnClose).setOnClickListener { goHome() }

        findViewById<View>(R.id.btnStart).setOnClickListener {
            val notify = findViewById<MaterialSwitch>(R.id.switchNotifications).isChecked
            getSharedPreferences("settings", Context.MODE_PRIVATE)
                .edit().putBoolean("notify_trial_end", notify).apply()
            billing.launchPurchase(this)
        }

        findViewById<View>(R.id.tvTerms).setOnClickListener {
            openUrl("https://sites.google.com/view/gps-explorer/terms")
        }
        findViewById<View>(R.id.tvPrivacy).setOnClickListener {
            openUrl("https://sites.google.com/view/gps-explorer/privacy")
        }

        findViewById<View>(R.id.tvRestore).setOnClickListener { billing.restore() }
    }

    private fun goHome() {
        startActivity(Intent(this, GiftActivity::class.java))
        finish()
    }

    private fun openUrl(url: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }
}
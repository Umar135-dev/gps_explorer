package com.example.gps_explorer

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat

class ProActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pro)
        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = true

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.root)) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        // The "Explore Like a Pro" heading (with blue "Pro") is now defined in the layout
        // (tvTitle + tvTitleAccent), so no title code is needed here.

        val oldPrice = findViewById<TextView>(R.id.tvOldPrice)
        oldPrice.paintFlags = oldPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

        findViewById<View>(R.id.btnClose).setOnClickListener { goHome() }

        findViewById<View>(R.id.btnContinue).setOnClickListener {
            // TODO: start the Google Play Billing subscription flow here
            Toast.makeText(this, "Billing not connected yet", Toast.LENGTH_SHORT).show()
        }

        findViewById<View>(R.id.tvTerms).setOnClickListener {
            openUrl("https://sites.google.com/view/gps-explorer/terms")
        }
        findViewById<View>(R.id.tvPrivacy).setOnClickListener {
            openUrl("https://sites.google.com/view/gps-explorer/privacy")
        }
        findViewById<View>(R.id.tvRestore).setOnClickListener {
            // TODO: check Google Play Billing for existing purchases
            Toast.makeText(this, "Restore not connected yet", Toast.LENGTH_SHORT).show()
        }
    }

    private fun goHome() {
        // Onboarding is finished only once the user leaves the last screen (now the Pro screen)
        getSharedPreferences("onboarding", Context.MODE_PRIVATE)
            .edit().putBoolean("completed", true).apply()

        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun openUrl(url: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }
}
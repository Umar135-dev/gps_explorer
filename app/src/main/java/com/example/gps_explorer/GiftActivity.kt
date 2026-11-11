package com.example.gps_explorer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.OvershootInterpolator
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat

class GiftActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_gift)
        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = true

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.root)) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        bounceGift()

        findViewById<View>(R.id.btnClose).setOnClickListener { goHome() }
        findViewById<View>(R.id.btnNotNow).setOnClickListener { goHome() }

        findViewById<View>(R.id.btnOpen).setOnClickListener {
            getSharedPreferences("settings", Context.MODE_PRIVATE)
                .edit().putBoolean("gift_opened", true).apply()

            startActivity(Intent(this, OfferActivity::class.java))
            finish()
        }
    }

    /** The gift pops in with a small bounce. */
    private fun bounceGift() {
        val gift = findViewById<ImageView>(R.id.ivGift)
        gift.scaleX = 0.6f
        gift.scaleY = 0.6f
        gift.alpha = 0f
        gift.animate()
            .scaleX(1f).scaleY(1f).alpha(1f)
            .setDuration(600)
            .setInterpolator(OvershootInterpolator(1.4f))
            .start()
    }

    private fun goHome() {
        // Onboarding is finished only once the user leaves without opening the gift
        getSharedPreferences("onboarding", Context.MODE_PRIVATE)
            .edit().putBoolean("completed", true).apply()

        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
package com.example.gps_explorer

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.view.View
import android.view.animation.OvershootInterpolator
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

class OfferActivity : AppCompatActivity() {

    private var skipDialog: BottomSheetDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_offer)
        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = true

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.root)) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        // Strike through the old price
        val oldPrice = findViewById<TextView>(R.id.tvOldPrice)
        oldPrice.paintFlags = oldPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

        popInImage()

        // Cross button and the Back button both ask "Are you sure?"
        findViewById<View>(R.id.btnClose).setOnClickListener { showSkipSheet() }
        onBackPressedDispatcher.addCallback(this) { showSkipSheet() }

        findViewById<View>(R.id.btnClaim).setOnClickListener {
            getSharedPreferences("settings", Context.MODE_PRIVATE)
                .edit().putBoolean("offer_claimed", true).apply()

            // TODO: start the Google Play Billing flow for the $11.99 first-year offer
            Toast.makeText(this, "Billing not connected yet", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showSkipSheet() {
        if (skipDialog?.isShowing == true) return

        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.dialog_skip_offer, null)
        dialog.setContentView(view)

        // Let the rounded top corners of our own background show
        (view.parent as? View)?.setBackgroundColor(Color.TRANSPARENT)

        dialog.behavior.skipCollapsed = true
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED

        view.findViewById<View>(R.id.btnBackToOffer).setOnClickListener { dialog.dismiss() }
        view.findViewById<View>(R.id.btnContinueWithout).setOnClickListener {
            dialog.dismiss()
            startActivity(Intent(this, LifetimeActivity::class.java))
            finish()
        }

        skipDialog = dialog
        dialog.show()
    }

    private fun popInImage() {
        val image = findViewById<ImageView>(R.id.ivOffer)
        image.scaleX = 0.7f
        image.scaleY = 0.7f
        image.alpha = 0f
        image.animate()
            .scaleX(1f).scaleY(1f).alpha(1f)
            .setDuration(600)
            .setInterpolator(OvershootInterpolator(1.3f))
            .start()
    }

    override fun onDestroy() {
        skipDialog?.dismiss()
        super.onDestroy()
    }
}
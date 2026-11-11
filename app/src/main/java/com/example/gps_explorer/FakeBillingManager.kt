package com.example.gps_explorer

import android.app.Activity
import android.content.Context

class FakeBillingManager(context: Context) : BillingService {
    override var listener: BillingListener? = null
    private val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)

    override fun connect(onReady: (String?) -> Unit) {
        onReady("\$29.99")
        restore()
    }

    override fun launchPurchase(activity: Activity) {
        prefs.edit().putBoolean("is_premium", true).apply()
        listener?.onMessage("Test purchase successful")
        listener?.onPremiumChanged(true)
    }

    override fun restore() {
        listener?.onPremiumChanged(prefs.getBoolean("is_premium", false))
    }
}
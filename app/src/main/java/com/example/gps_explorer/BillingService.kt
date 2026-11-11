package com.example.gps_explorer

import android.app.Activity

interface BillingListener {
    fun onPremiumChanged(isPremium: Boolean)
    fun onMessage(message: String)
}

interface BillingService {
    var listener: BillingListener?
    fun connect(onReady: (priceText: String?) -> Unit)
    fun launchPurchase(activity: Activity)
    fun restore()
}
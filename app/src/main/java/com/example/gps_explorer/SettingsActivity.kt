package com.example.gps_explorer

import android.content.ActivityNotFoundException
import android.content.Intent
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
import com.google.android.material.materialswitch.MaterialSwitch
import java.util.Locale
import androidx.appcompat.app.AppCompatDelegate

class SettingsActivity : AppCompatActivity() {

    private val prefs by lazy { getSharedPreferences("settings", MODE_PRIVATE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)
        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = true

        // Keep content clear of the status bar and navigation bar
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.root)) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        findViewById<View>(R.id.btnBack).setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // Pro banner
        findViewById<View>(R.id.btnUnlockPro).setOnClickListener {
            startActivity(Intent(this, ProActivity::class.java))
        }

        // Preferences
        findViewById<View>(R.id.rowLanguage).setOnClickListener {
            startActivity(Intent(this, LanguageActivity::class.java))
        }

        val notifications = findViewById<MaterialSwitch>(R.id.switchNotifications)
        notifications.isChecked = prefs.getBoolean(KEY_NOTIFICATIONS, true)
        notifications.setOnCheckedChangeListener { _, checked ->
            // TODO: on Android 13+ also ask for the POST_NOTIFICATIONS permission when turning this on
            prefs.edit().putBoolean(KEY_NOTIFICATIONS, checked).apply()
        }
        findViewById<View>(R.id.rowNotifications).setOnClickListener { notifications.toggle() }

        // Support & more
        findViewById<View>(R.id.rowHelp).setOnClickListener { sendFeedback() }
        findViewById<View>(R.id.rowRate).setOnClickListener { rateApp() }
        findViewById<View>(R.id.rowShare).setOnClickListener { shareApp() }
        findViewById<View>(R.id.rowPrivacy).setOnClickListener { openUrl(PRIVACY_URL) }

        // Footer: "GPSExplore • Version 1.0.0"
        val versionName = runCatching {
            packageManager.getPackageInfo(packageName, 0).versionName
        }.getOrNull() ?: "1.0.0"
        findViewById<TextView>(R.id.tvVersion).text = getString(R.string.settings_version, versionName)
    }

    override fun onResume() {
        super.onResume()
        // Shown again after coming back from the language screen
        findViewById<TextView>(R.id.tvLanguageValue).text = currentLanguageName()
    }

    /** The saved language, written in its own language (e.g. "English", "Español"). */
    private fun currentLanguageName(): String {
        val applied = AppCompatDelegate.getApplicationLocales()
        val tag = if (!applied.isEmpty) applied.toLanguageTags()
        else prefs.getString("language_tag", "en") ?: "en"
        val locale = Locale.forLanguageTag(tag)
        return locale.getDisplayName(locale).replaceFirstChar { it.titlecase(locale) }
    }

    private fun sendFeedback() {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(SUPPORT_EMAIL))
            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.settings_feedback_subject))
        }
        startSafely(intent)
    }

    private fun rateApp() {
        try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")))
        } catch (e: ActivityNotFoundException) {
            openUrl("https://play.google.com/store/apps/details?id=$packageName")
        }
    }

    private fun shareApp() {
        val link = "https://play.google.com/store/apps/details?id=$packageName"
        val send = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, getString(R.string.settings_share_text, link))
        }
        startActivity(Intent.createChooser(send, getString(R.string.settings_share_title)))
    }

    private fun openUrl(url: String) {
        startSafely(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }

    private fun startSafely(intent: Intent) {
        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, R.string.settings_no_app, Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val KEY_NOTIFICATIONS = "notifications_enabled"

        // TODO: replace with your real support email and privacy policy page
        private const val SUPPORT_EMAIL = "support@example.com"
        private const val PRIVACY_URL = "https://example.com/privacy"
    }
}

package com.example.gps_explorer

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

data class Language(val name: String, val nativeName: String, val tag: String)

class LanguageActivity : AppCompatActivity() {

    // Add more languages here, e.g. Language("Urdu", "اردو", "ur")
    private val allLanguages = listOf(
        Language("English", "English", "en"),
        Language("Spanish", "Español", "es"),
        Language("French", "Français", "fr"),
        Language("German", "Deutsch", "de"),
        Language("Arabic", "العربية", "ar"),
        Language("Portuguese", "Português", "pt"),
        Language("Hindi", "हिन्दी", "hi"),
    )

    private lateinit var selected: Language
    private lateinit var adapter: LanguageAdapter
    private var query = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_language)
        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = true

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.root)) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                v.paddingLeft.coerceAtLeast(bars.left), bars.top,
                v.paddingRight.coerceAtLeast(bars.right), bars.bottom
            )
            insets
        }

        // Prefer the language actually applied to the app, then the saved preference
        val prefs = getSharedPreferences("settings", MODE_PRIVATE)
        val applied = AppCompatDelegate.getApplicationLocales()
        val currentTag = if (!applied.isEmpty) {
            applied.get(0)?.language
        } else {
            prefs.getString("language_tag", "en")
        }
        selected = allLanguages.firstOrNull { it.tag == currentTag } ?: allLanguages.first()

        adapter = LanguageAdapter { language ->
            selected = language
            refresh()
        }
        findViewById<RecyclerView>(R.id.rvLanguages).apply {
            layoutManager = LinearLayoutManager(this@LanguageActivity)
            adapter = this@LanguageActivity.adapter
        }

        findViewById<EditText>(R.id.etSearch).doAfterTextChanged {
            query = it?.toString().orEmpty().trim()
            refresh()
        }

        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        findViewById<View>(R.id.btnApply).setOnClickListener {
            if (intent.getBooleanExtra("from_onboarding", false)) {
                startActivity(Intent(this, PremiumActivity::class.java))
            }
            finish()
            applyLanguage(selected.tag)   // last, so it recreates the other open screens
        }

        refresh()
    }

    /** Saves the choice (so Settings can show the name) and switches the whole app. */
    private fun applyLanguage(tag: String) {
        getSharedPreferences("settings", MODE_PRIVATE)
            .edit().putString("language_tag", tag).apply()

        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(tag))
    }

    private fun refresh() {
        findViewById<TextView>(R.id.tvSelName).text = selected.name
        findViewById<TextView>(R.id.tvSelNative).text = selected.nativeName

        val items = allLanguages.filter {
            it.tag != selected.tag && (
                    query.isEmpty() ||
                            it.name.contains(query, ignoreCase = true) ||
                            it.nativeName.contains(query, ignoreCase = true)
                    )
        }
        adapter.submit(items)
    }
}

private class LanguageAdapter(
    private val onClick: (Language) -> Unit
) : RecyclerView.Adapter<LanguageAdapter.VH>() {

    private var items: List<Language> = emptyList()

    fun submit(newItems: List<Language>) {
        items = newItems
        notifyDataSetChanged()
    }

    class VH(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.tvName)
        val native: TextView = view.findViewById(R.id.tvNative)
        val divider: View = view.findViewById(R.id.divider)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
        VH(LayoutInflater.from(parent.context).inflate(R.layout.item_language, parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        holder.name.text = item.name
        holder.native.text = item.nativeName
        holder.divider.visibility = if (position == items.lastIndex) View.GONE else View.VISIBLE
        holder.itemView.setOnClickListener { onClick(item) }
    }

    override fun getItemCount() = items.size
}
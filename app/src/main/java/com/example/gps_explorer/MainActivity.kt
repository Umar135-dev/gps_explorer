package com.example.gps_explorer

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction


class MainActivity : AppCompatActivity() {

    private enum class Tab { HOME, PLANNER, TOOLS }

    private var currentTab = Tab.HOME

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = true

        // Keep content clear of the status bar and navigation bar
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.root)) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        if (savedInstanceState == null) {
            showFragment(HomeFragment(), "home")
        } else {
            // Fragments are restored automatically; just restore which tab was selected
            currentTab = Tab.values()[savedInstanceState.getInt(KEY_TAB, 0)]
        }
        updateNav()

        // Bottom navigation
        findViewById<View>(R.id.navHome).setOnClickListener { selectTab(Tab.HOME) }
        findViewById<View>(R.id.navPlanner).setOnClickListener { selectTab(Tab.PLANNER) }
        findViewById<View>(R.id.navTools).setOnClickListener { selectTab(Tab.TOOLS) }
        findViewById<View>(R.id.navPro).setOnClickListener {
            startActivity(Intent(this, ProActivity::class.java))
        }
        findViewById<View>(R.id.fabAi).setOnClickListener {
            // TODO: open the AI assistant
            Toast.makeText(this, "Coming soon", Toast.LENGTH_SHORT).show()
        }

        // Back on Planner or Tools returns to Home; back on Home leaves the app
        onBackPressedDispatcher.addCallback(this) {
            if (currentTab != Tab.HOME) {
                selectTab(Tab.HOME)
            } else {
                isEnabled = false
                onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_TAB, currentTab.ordinal)
    }

    private fun selectTab(tab: Tab) {
        if (tab == currentTab) return
        currentTab = tab
        when (tab) {
            Tab.HOME -> showFragment(HomeFragment(), "home")
            Tab.PLANNER -> showFragment(PlannerFragment(), "planner")
            Tab.TOOLS -> showFragment(ToolsFragment(), "tools")
        }
        updateNav()
    }

    private fun showFragment(fragment: Fragment, tag: String) {
        supportFragmentManager.beginTransaction()
            .setReorderingAllowed(true)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .replace(R.id.fragmentContainer, fragment, tag)
            .commit()
    }

    /** Highlights the selected tab (blue circle, white icon, blue bold label). */
    private fun updateNav() {
        styleNavItem(
            R.id.navHomeIcon, R.id.navHomeLabel, currentTab == Tab.HOME,
            selectedIcon = R.drawable.home_ic_home,
            unselectedIcon = R.drawable.planner_ic_home_outline
        )
        styleNavItem(
            R.id.navPlannerIcon, R.id.navPlannerLabel, currentTab == Tab.PLANNER,
            selectedIcon = R.drawable.home_ic_planner,
            unselectedIcon = R.drawable.home_ic_planner
        )
        styleNavItem(
            R.id.navToolsIcon, R.id.navToolsLabel, currentTab == Tab.TOOLS,
            selectedIcon = R.drawable.home_ic_tools,
            unselectedIcon = R.drawable.home_ic_tools
        )
        // Pro opens a full-screen paywall, so it is never a selected tab
        styleNavItem(
            R.id.navProIcon, R.id.navProLabel, false,
            selectedIcon = R.drawable.home_ic_crown, unselectedIcon = R.drawable.home_ic_crown
        )
    }

    private fun styleNavItem(
        iconId: Int, labelId: Int, selected: Boolean,
        selectedIcon: Int, unselectedIcon: Int
    ) {
        val icon = findViewById<ImageView>(iconId)
        val label = findViewById<TextView>(labelId)

        icon.setImageResource(if (selected) selectedIcon else unselectedIcon)
        icon.background = if (selected) {
            ContextCompat.getDrawable(this, R.drawable.home_bg_nav_selected)
        } else null
        icon.imageTintList = ColorStateList.valueOf(
            if (selected) 0xFFFFFFFF.toInt()
            else ContextCompat.getColor(this, R.color.home_icon_dark)
        )

        label.setTextColor(
            ContextCompat.getColor(this, if (selected) R.color.screen_primary else R.color.home_icon_dark)
        )
        label.setTypeface(null, if (selected) Typeface.BOLD else Typeface.NORMAL)
    }

    companion object {
        private const val KEY_TAB = "current_tab"
    }
}

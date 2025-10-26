package com.example.aplicatie

import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.aplicatie.ui.leaderboard.LeaderboardFragment
import com.example.aplicatie.ui.profile.ProfileFragment
import com.google.android.material.switchmaterial.SwitchMaterial

class MainActivity : AppCompatActivity() {

    // Debounce so rapid taps on the switch won't flicker the UI
    private var isApplyingTheme = false
    private var lastToggleTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Read saved theme BEFORE setContentView to avoid initial flicker
        val prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val dark = prefs.getBoolean("dark_mode", false)

        // Apply local night mode without forcing a full recreate
        delegate.localNightMode =
            if (dark) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        delegate.applyDayNight()

        setContentView(R.layout.activity_main)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val currentUsername = prefs.getString("username", "ANONIM") ?: "ANONIM"

        // ===== Dark Mode switch (top-left) =====
        findViewById<SwitchMaterial>(R.id.theme_switch)?.apply {
            isChecked = dark
            setOnCheckedChangeListener { _, isChecked ->
                val now = SystemClock.uptimeMillis()
                if (now - lastToggleTime < 400 || isApplyingTheme) return@setOnCheckedChangeListener
                lastToggleTime = now
                isApplyingTheme = true

                val newMode = if (isChecked)
                    AppCompatDelegate.MODE_NIGHT_YES
                else
                    AppCompatDelegate.MODE_NIGHT_NO

                // Avoid re-applying the same mode
                if (delegate.localNightMode != newMode) {
                    prefs.edit().putBoolean("dark_mode", isChecked).apply()
                    delegate.localNightMode = newMode
                    delegate.applyDayNight() // apply theme without recreate
                }

                postDelayed({ isApplyingTheme = false }, 300)
            }
        }

        // ===== Default fragment: Profile (with username) =====
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            .replace(R.id.fragment_container, ProfileFragment.newInstance(currentUsername))
            .commit()

        // ===== Switch between fragments (with animation) =====
        findViewById<Button>(R.id.btn_profile).setOnClickListener {
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(
                    android.R.anim.fade_in, android.R.anim.fade_out,
                    android.R.anim.fade_in, android.R.anim.fade_out
                )
                .replace(R.id.fragment_container, ProfileFragment.newInstance(currentUsername))
                .addToBackStack(null)
                .commit()
        }

        findViewById<Button>(R.id.btn_leaderboard).setOnClickListener {
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(
                    android.R.anim.fade_in, android.R.anim.fade_out,
                    android.R.anim.fade_in, android.R.anim.fade_out
                )
                .replace(R.id.fragment_container, LeaderboardFragment())
                .addToBackStack(null)
                .commit()
        }

        // ===== Play -> ChooseDifficulty -> Quiz =====
        findViewById<Button>(R.id.play_button).setOnClickListener {
            val intent = Intent(this, com.example.aplicatie.ui.ChooseDifficultyActivity::class.java)
            intent.putExtra("username", currentUsername)
            startActivity(intent)
        }
    }
}

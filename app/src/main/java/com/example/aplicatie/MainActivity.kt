package com.example.aplicatie

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.core.content.edit
import androidx.lifecycle.lifecycleScope
import com.example.aplicatie.ui.MainScreen
import com.example.aplicatie.ui.SelectTopicActivity
import com.example.aplicatie.ui.theme.AplicatieTheme
import com.example.aplicatie.util.LocationLanguage
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
            // request perm location
        // înainte de setContent (sau imediat după), o singură dată la pornire:
        LocationLanguage.requestLocationPermission(this)

        lifecycleScope.launch {
            // salvează "ro"/"en"/"de" în SharedPreferences (LocationLanguage.PREFS/PREF_LANG)
            LocationLanguage.detectAndSaveLanguage(this@MainActivity)
        }

        // read saved theme before composing
        val prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val darkSaved = prefs.getBoolean("dark_mode", false)
        AppCompatDelegate.setDefaultNightMode(
            if (darkSaved) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )

        val currentUsername = prefs.getString("username", "ANONIM") ?: "ANONIM"

        setContent {
            // keep theme reactive so switch changes UI instantly
            var isDark by rememberSaveable { mutableStateOf(darkSaved) }

            // keep AppCompat in sync (for system bars, dialogs etc.)
            LaunchedEffect(isDark) {
                AppCompatDelegate.setDefaultNightMode(
                    if (isDark) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
                )
            }
            AplicatieTheme(darkTheme = isDark) {
                MainScreen(
                    username = currentUsername,
                    isDark = isDark,
                    onToggleDark = { checked ->
                        isDark = checked
                        prefs.edit { putBoolean("dark_mode", checked) }
                    },
                    onPlay = {
                        startActivity(
                            Intent(this@MainActivity, SelectTopicActivity::class.java).putExtra("username", currentUsername)
                        )
                    },
                    onOpenFullLeaderboard = {
                        startActivity(
                            Intent(
                                this@MainActivity,
                                com.example.aplicatie.ui.leaderboard.LeaderboardActivity::class.java
                            ).putExtra("username", currentUsername)
                        )
                    },
                    onOpenLearningMode = {
                        startActivity(
                            Intent(
                                this@MainActivity,
                                com.example.aplicatie.ui.learning.SelectLearningTopicActivity::class.java
                            )
                        )
                    },
                    onOpenAwards = {
                        startActivity(
                            Intent(
                                this@MainActivity,
                                com.example.aplicatie.ui.awards.AwardsActivity::class.java
                            )
                        )
                    }


                )
            }
        }
    }
}

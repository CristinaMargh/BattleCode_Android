package com.example.aplicatie.ui.multi

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.aplicatie.ui.theme.AplicatieTheme

class MultiplayerMenuActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val username = intent.getStringExtra("username") ?: "ANONIM"

        setContent {
            AplicatieTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MultiplayerMenuScreen(
                        onLocalMultiplayer = {
                            startActivity(
                                Intent(this, LocalMultiplayerQuizActivity::class.java).apply {
                                    putExtra("username", username)
                                    // opțional, dacă vrei să trimiți topic/difficulty:
                                    putExtra("topic", "cpp")
                                    putExtra("difficulty", "easy")
                                }
                            )
                        },
                        onBack = { finish() }
                    )
                }
            }
        }
    }
}

@Composable
private fun MultiplayerMenuScreen(
    onLocalMultiplayer: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Multiplayer", style = MaterialTheme.typography.headlineMedium)

        Button(
            onClick = onLocalMultiplayer,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Local Multiplayer (2 players)")
        }

        OutlinedButton(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back")
        }
    }
}

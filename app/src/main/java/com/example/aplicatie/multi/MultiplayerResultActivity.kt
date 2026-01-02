package com.example.aplicatie.ui.multi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.aplicatie.ui.theme.AplicatieTheme

class MultiplayerResultActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val p1 = intent.getIntExtra("p1Score", 0)
        val p2 = intent.getIntExtra("p2Score", 0)

        setContent {
            AplicatieTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MultiplayerResultScreen(
                        p1 = p1,
                        p2 = p2,
                        onBack = { finish() }
                    )
                }
            }
        }
    }
}

@Composable
private fun MultiplayerResultScreen(
    p1: Int,
    p2: Int,
    onBack: () -> Unit
) {
    val winner = when {
        p1 > p2 -> "Winner: Player 1"
        p2 > p1 -> "Winner: Player 2"
        else -> "Draw!"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Multiplayer Result", style = MaterialTheme.typography.headlineMedium)
        Text("Player 1: $p1", style = MaterialTheme.typography.titleLarge)
        Text("Player 2: $p2", style = MaterialTheme.typography.titleLarge)
        Text(winner, style = MaterialTheme.typography.headlineSmall)

        Spacer(Modifier.height(12.dp))

        Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
            Text("Back")
        }
    }
}

package com.example.aplicatie.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aplicatie.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

@Composable
fun MainScreen(
    username: String,
    isDark: Boolean,
    onToggleDark: (Boolean) -> Unit,
    onPlay: () -> Unit,
    onOpenFullLeaderboard: () -> Unit
) {
    var tab by remember { mutableStateOf(MainTab.Profile) }

    val lilac = Color(0xFF98A5D6)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(lilac)
    ) {
        // Dark switch
        Row(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Dark", fontSize = 12.sp)
            Spacer(Modifier.width(6.dp))
            Switch(checked = isDark, onCheckedChange = onToggleDark)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(24.dp))

            Image(
                painter = painterResource(R.drawable.battle_code_logo4),
                contentDescription = "Logo",
                modifier = Modifier.size(140.dp)   // <- mai mare
            )

            Text(
                text = "Battle Code",
                style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
            )

            Spacer(Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(onClick = { tab = MainTab.Profile }) { Text("Profile") }
                OutlinedButton(onClick = { tab = MainTab.Leaderboard }) { Text("Leaderboard") }
            }

            Spacer(Modifier.height(20.dp))

            // fragment
            when (tab) {
                MainTab.Profile -> ProfileCard(username)
                MainTab.Leaderboard -> LeaderboardPreview(onOpenFullLeaderboard)
            }

            Spacer(Modifier.height(28.dp))

            Button(
                onClick = onPlay,
                modifier = Modifier.width(200.dp)
            ) { Text("Play", fontSize = 18.sp) }
        }
    }
}


private enum class MainTab { Profile, Leaderboard }

@Composable
private fun ProfileCard(username: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Welcome, $username",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = "Ready to code? Choose a difficulty and start!",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun LeaderboardPreview(onOpenFullLeaderboard: () -> Unit) {
    var loading by remember { mutableStateOf(true) }
    var topName by remember { mutableStateOf<String?>(null) }
    var topScore by remember { mutableStateOf<Int?>(null) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        // Firestore: users ordered by highScore desc, take first
        FirebaseFirestore.getInstance()
            .collection("users")
            .orderBy("highScore", Query.Direction.DESCENDING)
            .limit(1)
            .get()
            .addOnSuccessListener { snap ->
                val doc = snap.documents.firstOrNull()
                topName = doc?.getString("username")
                topScore = (doc?.getLong("highScore") ?: 0L).toInt()
                loading = false
            }
            .addOnFailureListener { e ->
                error = e.localizedMessage
                loading = false
            }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when {
            loading -> {
                CircularProgressIndicator()
                Spacer(Modifier.height(8.dp))
                Text("Loading current winner...")
            }
            error != null -> {
                Text("Failed to load leaderboard")
                Text(error ?: "", fontSize = 12.sp)
                Spacer(Modifier.height(8.dp))
                TextButton(onClick = onOpenFullLeaderboard) {
                    Text("Open full leaderboard")
                }
            }
            else -> {
                Text(
                    text = "Current winner",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = "${topName ?: "—"} — ${topScore ?: 0} pts",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(Modifier.height(8.dp))
                TextButton(onClick = onOpenFullLeaderboard) {
                    Text("See full leaderboard")
                }
            }
        }
    }
}

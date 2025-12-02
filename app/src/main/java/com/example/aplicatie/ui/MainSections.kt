package com.example.aplicatie.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aplicatie.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import androidx.compose.ui.platform.LocalContext
import com.example.aplicatie.util.StreakManager
import androidx.compose.material3.Text



@Composable
fun MainScreen(
    username: String,
    isDark: Boolean,
    onToggleDark: (Boolean) -> Unit,
    onPlay: () -> Unit,
    onOpenFullLeaderboard: () -> Unit,
    onOpenLearningMode: () -> Unit,
    onOpenAwards: () -> Unit,
    onOpenStatistics: () -> Unit,
    onOpenFriends: () -> Unit
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


            val buttonModifier = Modifier
                .fillMaxWidth()
                .height(52.dp)

            val buttonTextStyle = TextStyle(fontSize = 18.sp)

            Button(
                onClick = onPlay,
                modifier = buttonModifier
            ) {
                Text("Play", style = buttonTextStyle)
            }

            Spacer(Modifier.height(12.dp))

            Button(
                onClick = onOpenLearningMode,
                modifier = buttonModifier
            ) {
                Text("Learning mode", style = buttonTextStyle)
            }

            Spacer(modifier = Modifier.height(12.dp))

            //  buton Awards
            Button(
                onClick = onOpenAwards,
                modifier = buttonModifier
            ) {
                Text("Awards", fontSize = 18.sp)
            }
            Spacer(Modifier.height(12.dp))

            Button(
                onClick = onOpenStatistics,
                modifier = buttonModifier
            ) {
                Text("Statistics", style = buttonTextStyle)
            }
            Spacer(Modifier.height(12.dp))

            Button(
                onClick = onOpenFriends,
                modifier = buttonModifier
            ) { Text("Friends", style = buttonTextStyle) }

        }
    }
}


private enum class MainTab { Profile, Leaderboard }

@Composable
private fun ProfileCard(username: String) {
    val ctx = LocalContext.current
    val streakDays by remember {
        mutableStateOf(StreakManager.getCurrentStreak(ctx))
    }
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
        Spacer(Modifier.height(16.dp))

        //  streak-ul
        val streakText =
            if (streakDays > 0)
                "Current streak: $streakDays day" + if (streakDays > 1) "s 🔥" else " 🔥"
            else
                "No streak yet — complete a quiz today!"

        Text(
            text = streakText,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
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

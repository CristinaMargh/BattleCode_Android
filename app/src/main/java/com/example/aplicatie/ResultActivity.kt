package com.example.aplicatie

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import com.example.aplicatie.util.StreakManager
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aplicatie.data.UserRepository
import com.example.aplicatie.ui.WrongAnswersActivity
import com.example.aplicatie.ui.auth.WelcomeActivity
import com.example.aplicatie.ui.leaderboard.LeaderboardActivity
import com.example.aplicatie.ui.theme.AplicatieTheme

class ResultActivity : ComponentActivity() {
    private val repo = UserRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val score = intent.getIntExtra("score", 0)
        val wrongQ = intent.getStringArrayListExtra("wrongQuestions") ?: arrayListOf()
        val wrongA = intent.getStringArrayListExtra("wrongCorrectAnswers") ?: arrayListOf()

        val prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val username = prefs.getString("username", "ANONIM") ?: "ANONIM"
        //am facut un quiz azi
        StreakManager.onQuizFinished(this, username = username)

        setContent {
            AplicatieTheme {
                Surface {
                    ResultScreen(
                        score = score,
                        username = username,
                        repo = repo,
                        wrongQuestions = wrongQ,
                        wrongCorrectAnswers = wrongA,
                        onPlayAgain = {
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        },
                        onLeaderboard = {
                            startActivity(
                                Intent(this, LeaderboardActivity::class.java)
                                    .putExtra("username", username)
                            )
                        },
                        onWrong = {
                            startActivity(
                                Intent(this, WrongAnswersActivity::class.java)
                                    .putStringArrayListExtra("wrongQuestions", wrongQ)
                                    .putStringArrayListExtra("wrongCorrectAnswers", wrongA)
                            )
                        },
                        onLogout = {
                            prefs.edit().remove("username").apply()
                            val i = Intent(this, WelcomeActivity::class.java)
                            i.flags =
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(i)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun ResultScreen(
    score: Int,
    username: String?,
    repo: UserRepository,
    wrongQuestions: List<String>,
    wrongCorrectAnswers: List<String>,
    onPlayAgain: () -> Unit,
    onLeaderboard: () -> Unit,
    onWrong: () -> Unit,
    onLogout: () -> Unit
) {
    // light purple background
    val lightPurple = Color(0xFF98A5D6)

    var high by remember { mutableStateOf<Int?>(null) }

    // fetch & update high score once
    LaunchedEffect(username, score) {
        if (username != null) {
            repo.getHighScore(username) { hs ->
                if (hs == null || score > hs) {
                    repo.updateHighScore(username, score)
                    high = score
                } else {
                    high = hs
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(lightPurple)
            .padding(24.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Results",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Final score: $score",
                fontSize = 22.sp,
                color = Color.Black
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "High score: ${high ?: "loading…"}",
                fontSize = 18.sp,
                color = Color.Black
            )

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = onPlayAgain,
                modifier = Modifier.fillMaxWidth()
            ) { Text("Play Again") }

            Spacer(Modifier.height(12.dp))

            Button(
                onClick = onWrong,
                modifier = Modifier.fillMaxWidth()
            ) { Text("Wrong Questions") }

            Spacer(Modifier.height(12.dp))

            Button(
                onClick = onLeaderboard,
                modifier = Modifier.fillMaxWidth()
            ) { Text("View Leaderboard") }

            Spacer(Modifier.height(12.dp))

            OutlinedButton(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth()
            ) { Text("Logout") }
        }
    }
}

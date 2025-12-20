package com.example.aplicatie.ui

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import com.example.aplicatie.data.QuizStats
import com.example.aplicatie.ui.theme.AplicatieTheme
import com.example.aplicatie.util.StatsStorage

class StatisticsActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val username = intent.getStringExtra("username")
            ?: prefs.getString("username", "ANONIM") ?: "ANONIM"

        // 🔹 încărcăm stats serializate (JSON -> QuizStats)
        val stats: QuizStats = StatsStorage.load(this, username)

        setContent {
            AplicatieTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    StatisticsScreen(
                        username = username,
                        stats = stats,
                        onBack = { finish() }
                    )
                }
            }
        }
    }
}

@Composable
fun StatisticsScreen(
    username: String,
    stats: QuizStats,
    onBack: () -> Unit
) {
    val lilac = Color(0xFF98A5D6)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(lilac)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Statistics",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(8.dp))
        Text(text = username, fontSize = 18.sp)

        Spacer(Modifier.height(24.dp))

        if (stats.totalQuizzes == 0) {
            Text(
                text = "No quiz data yet.\nPlay your first quiz to see stats here!",
                fontSize = 16.sp
            )
        } else {
            StatRow("Total quizzes", stats.totalQuizzes.toString())
            StatRow("Last score", stats.lastScore.toString())
            StatRow("Best score", stats.bestScore.toString())
            StatRow("Total questions", stats.totalAnswers.toString())
            StatRow("Correct answers", stats.correctAnswers.toString())
            StatRow("Accuracy", "${(stats.accuracy * 100).toInt()}%")
        }

        Spacer(Modifier.weight(1f))

        Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
            Text("Back")
        }
    }
}

@Composable
private fun StatRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 16.sp)
        Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
    }
}

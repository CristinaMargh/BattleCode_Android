package com.example.aplicatie.ui

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aplicatie.ui.theme.AplicatieTheme
import com.google.firebase.firestore.FirebaseFirestore

class StatisticsActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val username = intent.getStringExtra("username")
            ?: prefs.getString("username", "ANONIM") ?: "ANONIM"

        setContent {
            AplicatieTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    StatisticsScreen(username = username) {
                        finish()
                    }
                }
            }
        }
    }
}

data class UserStats(
    val totalQuizzes: Int,
    val lastScore: Int,
    val highScore: Int,
    val totalQuestions: Int,
    val totalCorrect: Int
) {
    val accuracy: Int
        get() = if (totalQuestions == 0) 0
        else ((totalCorrect * 100.0) / totalQuestions).toInt()
}

@Composable
fun StatisticsScreen(
    username: String,
    onBack: () -> Unit
) {
    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    var stats by remember { mutableStateOf<UserStats?>(null) }

    LaunchedEffect(username) {
        FirebaseFirestore.getInstance()
            .collection("users")
            .document(username)
            .get()
            .addOnSuccessListener { doc ->
                if (!doc.exists()) {
                    error = "No stats found for $username"
                } else {
                    val totalQuizzes = (doc.getLong("totalQuizzes") ?: 0L).toInt()
                    val lastScore = (doc.getLong("lastScore") ?: 0L).toInt()
                    val highScore = (doc.getLong("highScore") ?: 0L).toInt()
                    val totalQuestions = (doc.getLong("totalQuestions") ?: 0L).toInt()
                    val totalCorrect = (doc.getLong("totalCorrect") ?: 0L).toInt()

                    stats = UserStats(
                        totalQuizzes = totalQuizzes,
                        lastScore = lastScore,
                        highScore = highScore,
                        totalQuestions = totalQuestions,
                        totalCorrect = totalCorrect
                    )
                }
                loading = false
            }
            .addOnFailureListener { e ->
                error = e.localizedMessage
                loading = false
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
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

        when {
            loading -> {
                CircularProgressIndicator()
            }
            error != null -> {
                Text("Error: ${error}", color = MaterialTheme.colorScheme.error)
            }
            stats != null -> {
                StatRow("Total quizzes", stats!!.totalQuizzes.toString())
                StatRow("Last score", stats!!.lastScore.toString())
                StatRow("Best score", stats!!.highScore.toString())
                StatRow("Total questions", stats!!.totalQuestions.toString())
                StatRow("Correct answers", stats!!.totalCorrect.toString())
                StatRow("Accuracy", "${stats!!.accuracy}%")
            }
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

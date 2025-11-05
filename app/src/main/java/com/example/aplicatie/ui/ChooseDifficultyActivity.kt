package com.example.aplicatie.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aplicatie.QuizActivity
import com.example.aplicatie.ui.theme.AplicatieTheme

class ChooseDifficultyActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val username = intent.getStringExtra("username") ?: "ANONIM"
        val topic = intent.getStringExtra("topic") ?: "cpp"

        setContent {
            AplicatieTheme {
                // 🔹 acest Box forțează fundalul mov deschis
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFD1C5E4)) // 💜 mov deschis aplicat direct
                ) {
                    ChooseDifficultyScreen(
                        onSelectDifficulty = { difficulty ->
                            startActivity(
                                Intent(this@ChooseDifficultyActivity, QuizPrepActivity::class.java)
                                    .putExtra("username", username)
                                    .putExtra("topic", topic)
                                    .putExtra("difficulty", difficulty)
                            )
                            finish()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ChooseDifficultyScreen(onSelectDifficulty: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Select Difficulty",
            fontSize = 28.sp,
            color = Color(0xFF4A148C), // mov închis pentru contrast
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(40.dp))

        // 🟢 EASY
        Button(
            onClick = { onSelectDifficulty("easy") },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)), // verde
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text("Easy", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 🟡 MEDIUM
        Button(
            onClick = { onSelectDifficulty("medium") },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC107)), // galben
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text("Medium", color = Color.Black, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 🔴 HARD
        Button(
            onClick = { onSelectDifficulty("hard") },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336)), // roșu
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text("Hard", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

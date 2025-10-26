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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aplicatie.QuizActivity

class ChooseDifficultyActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                DifficultyScreen { difficulty ->
                    val intent = Intent(this, QuizActivity::class.java)
                    intent.putExtra("difficulty", difficulty)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }
}

@Composable
fun DifficultyScreen(onSelect: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF98A5D6))
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Alege dificultatea", fontSize = 28.sp, color = Color.Black)
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { onSelect("easy") },
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB4D69E))
        ) { Text("Easy", fontSize = 20.sp, color = Color.Black) }

        Button(
            onClick = { onSelect("medium") },
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF6E49C))
        ) { Text("Medium", fontSize = 20.sp, color = Color.Black) }

        Button(
            onClick = { onSelect("hard") },
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF59A9A))
        ) { Text("Hard", fontSize = 20.sp, color = Color.Black) }
    }
}

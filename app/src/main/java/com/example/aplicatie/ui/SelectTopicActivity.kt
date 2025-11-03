package com.example.aplicatie.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aplicatie.ui.theme.AplicatieTheme
import com.example.aplicatie.ui.ChooseDifficultyActivity

class SelectTopicActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val username = intent.getStringExtra("username")

        setContent {
            AplicatieTheme {
                TopicScreen(
                    onTopicChosen = { topic ->
                        startActivity(
                            Intent(this, ChooseDifficultyActivity::class.java)
                                .putExtra("username", username)
                                .putExtra("topic", topic)
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun TopicScreen(onTopicChosen: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF98A5D6))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Choose language/topic", fontSize = 26.sp, color = Color.Black)
        Spacer(Modifier.height(24.dp))

        Button(
            onClick = { onTopicChosen("cpp") },
            modifier = Modifier.fillMaxWidth()
        ) { Text("C++") }

        Spacer(Modifier.height(12.dp))

        Button(
            onClick = { onTopicChosen("java") },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Java") }

        Spacer(Modifier.height(12.dp))

        Button(
            onClick = { onTopicChosen("python") },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Python") }
    }
}

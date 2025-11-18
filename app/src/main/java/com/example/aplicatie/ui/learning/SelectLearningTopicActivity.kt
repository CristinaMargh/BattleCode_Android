package com.example.aplicatie.ui.learning

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aplicatie.ui.theme.AplicatieTheme

class SelectLearningTopicActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AplicatieTheme {
                Surface {
                    SelectLearningTopicScreen(
                        onSelectTopic = { topic ->
                            startActivity(
                                Intent(this, LearningModeActivity::class.java)
                                    .putExtra("topic", topic)
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
fun SelectLearningTopicScreen(
    onSelectTopic: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF98A5D6)) // același mov deschis ca în app
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Choose a topic to learn",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = { onSelectTopic("cpp") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text("C++", fontSize = 18.sp)
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = { onSelectTopic("java") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text("Java", fontSize = 18.sp)
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = { onSelectTopic("python") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text("Python", fontSize = 18.sp)
            }
        }
    }
}

package com.example.aplicatie.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aplicatie.QuizActivity
import com.example.aplicatie.R
import kotlinx.coroutines.delay

class QuizPrepActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val username = intent.getStringExtra("username") ?: "ANONIM"
        val topic = intent.getStringExtra("topic") ?: "cpp"
        val difficulty = intent.getStringExtra("difficulty") ?: "medium"

        setContent {
            // movul tău pal
            val bg = Color(0xFF98A5D6)

            // countdown state
            var counter by remember { mutableStateOf(3) }

            // când intrăm pe ecran, pornim timerul și la final mergem în QuizActivity
            LaunchedEffect(Unit) {
                while (counter > 1) {
                    delay(1000)
                    counter -= 1
                }
                // după 1 sec mai pornim quiz-ul
                delay(1000)
                startActivity(
                    Intent(this@QuizPrepActivity, QuizActivity::class.java)
                        .putExtra("username", username)
                        .putExtra("topic", topic)
                        .putExtra("difficulty", difficulty)
                )
                finish()
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(bg)
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // logo sus
                    Image(
                        painter = painterResource(id = R.drawable.battle_code_logo4),
                        contentDescription = "Logo",
                        modifier = Modifier
                            .size(140.dp)
                    )

                    Text(
                        text = "BattleCode",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    // mesajul dinamic
                    val langLabel = when (topic.lowercase()) {
                        "cpp" -> "C++"
                        "java" -> "Java"
                        "python" -> "Python"
                        else -> "coding"
                    }

                    Text(
                        text = "Getting ready for the $langLabel quiz",
                        fontSize = 20.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = counter.toString(),
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    Text(
                        text = "Difficulty: $difficulty",
                        fontSize = 16.sp,
                        color = Color(0xFF1B1B1B)
                    )
                }
            }
        }
    }
}

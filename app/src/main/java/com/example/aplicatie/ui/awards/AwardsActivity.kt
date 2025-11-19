package com.example.aplicatie.ui.awards

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aplicatie.R
import com.example.aplicatie.ui.theme.AplicatieTheme

class AwardsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val username = prefs.getString("username", "ANONIM") ?: "ANONIM"

        // citim dacă a terminat modulele
        val cppUnlocked = prefs.getBoolean("award_${username}_cpp", false)
        val javaUnlocked = prefs.getBoolean("award_${username}_java", false)
        val pythonUnlocked = prefs.getBoolean("award_${username}_python", false)

        setContent {
            AplicatieTheme {
                AwardsScreen(
                    cppUnlocked = cppUnlocked,
                    javaUnlocked = javaUnlocked,
                    pythonUnlocked = pythonUnlocked
                )
            }
        }
    }
}

@Composable
fun AwardsScreen(
    cppUnlocked: Boolean,
    javaUnlocked: Boolean,
    pythonUnlocked: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF98A5D6)) // movul tău
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Happy cat sus
            Image(
                painter = painterResource(id = R.drawable.happy_cat),
                contentDescription = "Happy cat",
                modifier = Modifier
                    .size(160.dp)
                    .padding(top = 8.dp)
            )

            Text(
                text = "Your awards",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(Modifier.height(16.dp))

            AwardCard(
                title = "C++ Learning Champion",
                description = "Completed all C++ learning questions.",
                unlocked = cppUnlocked,
                color = Color(0xFF4CAF50) // verde
            )

            Spacer(Modifier.height(8.dp))

            AwardCard(
                title = "Java Learning Champion",
                description = "Completed all Java learning questions.",
                unlocked = javaUnlocked,
                color = Color(0xFFFFC107) // galben
            )

            Spacer(Modifier.height(8.dp))

            AwardCard(
                title = "Python Learning Champion",
                description = "Completed all Python learning questions.",
                unlocked = pythonUnlocked,
                color = Color(0xFF2196F3) // albastru
            )
        }
    }
}

@Composable
private fun AwardCard(
    title: String,
    description: String,
    unlocked: Boolean,
    color: Color
) {
    val bg = if (unlocked) color else Color.LightGray
    val textColor = if (unlocked) Color.White else Color.DarkGray
    val label = if (unlocked) "UNLOCKED 🏆" else "Locked"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 80.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = bg)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = textColor)
            Spacer(Modifier.height(4.dp))
            Text(description, fontSize = 14.sp, color = textColor)
            Spacer(Modifier.height(4.dp))
            Text(label, fontSize = 12.sp, color = textColor)
        }
    }
}

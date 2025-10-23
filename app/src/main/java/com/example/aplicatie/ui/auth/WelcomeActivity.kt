package com.example.aplicatie.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aplicatie.ui.theme.AplicatieTheme

class WelcomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AplicatieTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WelcomeScreen(
                        onLoginClick = {
                            startActivity(Intent(this, LoginActivity::class.java))
                        },
                        onRegisterClick = {
                            startActivity(Intent(this, RegisterActivity::class.java))
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun WelcomeScreen(onLoginClick: () -> Unit, onRegisterClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Welcome to BattleCode!",
            style = MaterialTheme.typography.headlineMedium,
            fontSize = 28.sp
        )
        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = onLoginClick,
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .height(50.dp)
        ) {
            Text("Login", fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = onRegisterClick,
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .height(50.dp)
        ) {
            Text("Register", fontSize = 18.sp)
        }
    }
}

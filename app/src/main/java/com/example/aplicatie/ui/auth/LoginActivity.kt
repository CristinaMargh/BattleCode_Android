package com.example.aplicatie.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aplicatie.MainActivity
import com.example.aplicatie.data.UserRepository
import com.example.aplicatie.ui.theme.AplicatieTheme

class LoginActivity : ComponentActivity() {
    private val repo = UserRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AplicatieTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    LoginScreen(
                        onLogin = { user, pass ->
                            repo.login(user, pass) { ok, err ->
                                if (ok) {
                                    val prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
                                    prefs.edit().putString("username", user).apply()
                                    startActivity(Intent(this, MainActivity::class.java))
                                    finish()
                                } else {
                                    Toast.makeText(this, err ?: "Login failed", Toast.LENGTH_SHORT).show()
                                }
                            }
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
fun LoginScreen(onLogin: (String, String) -> Unit, onRegisterClick: () -> Unit) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Login", fontSize = 28.sp)
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") }
        )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = { onLogin(username, password) },
            modifier = Modifier.fillMaxWidth(0.6f)
        ) {
            Text("Login")
        }
        Spacer(modifier = Modifier.height(16.dp))
        TextButton(onClick = onRegisterClick) {
            Text("Go to Register")
        }
    }
}

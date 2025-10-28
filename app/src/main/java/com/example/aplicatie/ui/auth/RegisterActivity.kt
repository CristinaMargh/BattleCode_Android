package com.example.aplicatie.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aplicatie.MainActivity
import com.example.aplicatie.data.UserRepository
import com.example.aplicatie.ui.theme.AplicatieTheme

class RegisterActivity : ComponentActivity() {

    private val repo = UserRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AplicatieTheme {
                // Whole screen uses the app background from your theme
                Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    RegisterScreen(
                        onLoginClick = {
                            startActivity(Intent(this, LoginActivity::class.java))
                            finish()
                        },
                        onSuccess = { username ->
                            val prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
                            prefs.edit().putString("username", username).apply()
                            startActivity(
                                Intent(this, MainActivity::class.java)
                                    .putExtra("username", username)
                            )
                            finish()
                        },
                        onError = { msg ->
                            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
                        },
                        doRegister = { user, pass, done ->
                            repo.register(user, pass) { ok, err ->
                                done(ok, err ?: "Registration failed")
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun RegisterScreen(
    onLoginClick: () -> Unit,
    onSuccess: (String) -> Unit,
    onError: (String) -> Unit,
    doRegister: (String, String, (Boolean, String) -> Unit) -> Unit,
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirm by remember { mutableStateOf("") }

    var passVisible by remember { mutableStateOf(false) }
    var confirmVisible by remember { mutableStateOf(false) }

    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Top bar with strong contrast (primary bg + white text)
        Text(
            text = "Create account",
            fontSize = 24.sp,                // a bit smaller
            color = Color.White,             // force white
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(24.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it; error = null },
            label = { Text("Username") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it; error = null },
            label = { Text("Password") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (passVisible) VisualTransformation.None
            else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { passVisible = !passVisible }) {
                    Icon(
                        imageVector = if (passVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                        contentDescription = if (passVisible) "Hide password" else "Show password"
                    )
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        )

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = confirm,
            onValueChange = { confirm = it; error = null },
            label = { Text("Confirm password") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (confirmVisible) VisualTransformation.None
            else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { confirmVisible = !confirmVisible }) {
                    Icon(
                        imageVector = if (confirmVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                        contentDescription = if (confirmVisible) "Hide password" else "Show password"
                    )
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        )

        if (error != null) {
            Spacer(Modifier.height(8.dp))
            Text(error!!, color = Color(0xFFB00020), fontSize = 13.sp)
        }

        Spacer(Modifier.height(20.dp))

        Button(
            onClick = {
                if (username.isBlank() || password.isBlank() || confirm.isBlank()) {
                    error = "Please fill in all fields"; return@Button
                }
                if (password.length < 6) {
                    error = "Password must be at least 6 characters"; return@Button
                }
                if (password != confirm) {
                    error = "Passwords do not match"; return@Button
                }

                loading = true
                doRegister(username, password) { ok, msg ->
                    loading = false
                    if (ok) onSuccess(username) else {
                        error = msg
                        onError(msg)
                    }
                }
            },
            enabled = !loading,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            if (loading) {
                CircularProgressIndicator(strokeWidth = 2.dp, color = Color.White)
            } else {
                Text("Register", fontSize = 18.sp)
            }
        }

        Spacer(Modifier.height(12.dp))

        TextButton(onClick = onLoginClick) {
            Text(
                "Already have an account? Login",
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

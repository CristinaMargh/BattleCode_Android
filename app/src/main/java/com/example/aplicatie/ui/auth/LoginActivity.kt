package com.example.aplicatie.ui.auth
import com.example.aplicatie.util.PushTokenManager

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.core.app.ActivityCompat
import com.example.aplicatie.MainActivity
import com.example.aplicatie.data.UserRepository
import com.example.aplicatie.ui.theme.AplicatieTheme
import androidx.core.content.ContextCompat
import android.Manifest

class LoginActivity : ComponentActivity() {

    private val repo = UserRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //cererea de permisiune notificare
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    1001
                )
            }
        }

        setContent {
            AplicatieTheme {
                Surface(Modifier.fillMaxSize()) {
                    LoginScreen(
                        onRegister = {
                            startActivity(Intent(this, RegisterActivity::class.java))
                        },
                        onSuccess = { username ->
                            // save + go to Main
                            val prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
                            prefs.edit().putString("username", username).apply()
                            PushTokenManager.refreshAndStoreToken(this, username)
                            startActivity(Intent(this, MainActivity::class.java)
                                .putExtra("username", username))
                            finish()
                        },
                        onError = { msg ->
                            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
                        },
                        doLogin = { user, pass, done ->
                            // call repository and return result to UI
                            repo.login(user, pass) { ok, err ->
                                done(ok, err ?: "Login failed")
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun LoginScreen(
    onRegister: () -> Unit,
    onSuccess: (String) -> Unit,
    onError: (String) -> Unit,
    doLogin: (String, String, (Boolean, String) -> Unit) -> Unit,
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Login",
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
            visualTransformation = if (passwordVisible) VisualTransformation.None
            else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                        contentDescription = if (passwordVisible) "Hide password" else "Show password"
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
                if (username.isBlank() || password.isBlank()) {
                    error = "Please fill in all fields"
                    return@Button
                }
                loading = true
                doLogin(username, password) { ok, msg ->
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
                Text("Login", fontSize = 18.sp)
            }
        }

        Spacer(Modifier.height(12.dp))

        TextButton(onClick = onRegister) {
            Text("Create an account", color = Color.White)
        }
    }
}

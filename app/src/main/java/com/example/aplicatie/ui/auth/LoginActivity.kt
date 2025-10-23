package com.example.aplicatie.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.aplicatie.R
import com.example.aplicatie.data.UserRepository
import com.example.aplicatie.MainActivity

class LoginActivity : AppCompatActivity() {
    private val repo = UserRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val username = findViewById<EditText>(R.id.login_username)
        val password = findViewById<EditText>(R.id.login_password)
        val loginBtn = findViewById<Button>(R.id.login_button)
        val registerLink = findViewById<TextView>(R.id.register_link)

        loginBtn.setOnClickListener {
            val user = username.text.toString().trim()
            val pass = password.text.toString().trim()

            if (user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // login logic
            repo.login(user, pass) { ok, err ->
                if (ok) {
                    // save username locally for later use
                    val prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
                    prefs.edit().putString("username", user).apply()

                    // move to MainActivity
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("username", user)
                    startActivity(intent)

                    // close LoginActivity so user can’t go back
                    finish()
                } else {
                    Toast.makeText(this, err ?: "Login failed", Toast.LENGTH_SHORT).show()
                }
            }
        }

        registerLink.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}

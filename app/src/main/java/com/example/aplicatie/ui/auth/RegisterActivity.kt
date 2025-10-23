//package com.example.aplicatie.ui.auth
//
//import android.os.Bundle
//import android.widget.*
//import androidx.appcompat.app.AppCompatActivity
//import com.example.aplicatie.data.UserRepository
//import com.example.aplicatie.ui.auth.LoginActivity
//
//class RegisterActivity : AppCompatActivity() {
//    private val repo = UserRepository()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        val username = EditText(this).apply { hint = "Username" }
//        val pass = EditText(this).apply { hint = "Password"; inputType = 129 }
//        val btn = Button(this).apply { text = "Register" }
//        val status = TextView(this)
//
//        btn.setOnClickListener {
//            repo.register(username.text.toString(), pass.text.toString()) { ok, err ->
//                status.text = if (ok) "Success! Go to login." else err
//                if (ok) finish() // închide și revii în Login
//            }
//        }
//
//        val layout = LinearLayout(this).apply {
//            orientation = LinearLayout.VERTICAL
//            setPadding(24, 24, 24, 24)
//            addView(username); addView(pass); addView(btn); addView(status)
//        }
//        setContentView(layout)
//    }
//}

package com.example.aplicatie.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.aplicatie.R
import com.example.aplicatie.data.UserRepository

class RegisterActivity : AppCompatActivity() {
    private val repo = UserRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val username = findViewById<EditText>(R.id.register_username)
        val password = findViewById<EditText>(R.id.register_password)
        val registerBtn = findViewById<Button>(R.id.register_button)
        val loginLink = findViewById<TextView>(R.id.login_link)

        registerBtn.setOnClickListener {
            val user = username.text.toString()
            val pass = password.text.toString()

            if (user.isBlank() || pass.isBlank()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            repo.register(user, pass) { ok, err ->
                if (ok) {
                    Toast.makeText(this, "Account created! You can now log in.", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, err, Toast.LENGTH_SHORT).show()
                }
            }
        }

        loginLink.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}

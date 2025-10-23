package com.example.aplicatie

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore

val db = FirebaseFirestore.getInstance()


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        val prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val currentUsername = prefs.getString("username", "ANONIM") ?: "ANONIM"
        setContentView(R.layout.activity_main)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val playButton: Button = findViewById(R.id.play_button)
        playButton.setOnClickListener {
            val intent = Intent(this, QuizActivity::class.java)
            intent.putExtra("username", currentUsername)
            startActivity(intent)
        }

    }
}

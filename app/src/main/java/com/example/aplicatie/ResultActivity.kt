//package com.example.aplicatie
//
//import android.content.Intent
//import android.os.Bundle
//import android.widget.Button
//import android.widget.TextView
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import com.example.aplicatie.ui.auth.WelcomeActivity
//import com.example.aplicatie.ui.leaderboard.LeaderboardActivity
//import com.google.firebase.firestore.FirebaseFirestore
//
//class ResultActivity : AppCompatActivity() {
//    private lateinit var firestore: FirebaseFirestoreris
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_result)
//
//        firestore = FirebaseFirestore.getInstance()
//        val score = intent.getIntExtra("score", 0)
//        val resultText = findViewById<TextView>(R.id.result_text)
//        val highScoreText = findViewById<TextView>(R.id.high_score_text)
//        val playAgainButton = findViewById<Button>(R.id.play_again_button)
//        val leaderboardBtn = findViewById<Button>(R.id.leaderboard_button)
//        val logoutBtn = findViewById<Button>(R.id.logout_button)
//
//        val prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
//        val username = prefs.getString("username", null)
//
//        resultText.text = "Scor final: $score"
//
//        if (username != null) {
//            val userRef = firestore.collection("users").document(username)
//
//            userRef.get().addOnSuccessListener { doc ->
//                val currentHighScore = doc.getLong("highScore")?.toInt() ?: 0
//                if (score > currentHighScore) {
//                    userRef.update("highScore", score)
//                }
//                highScoreText.text = "Scor maxim: ${maxOf(score, currentHighScore)}"
//            }.addOnFailureListener {
//                highScoreText.text = "Scor maxim: necunoscut"
//                Toast.makeText(this, "Eroare la citirea scorului.", Toast.LENGTH_SHORT).show()
//            }
//        } else {
//            highScoreText.text = "Scor maxim: necunoscut"
//        }
//
//        playAgainButton.setOnClickListener {
//            startActivity(Intent(this, MainActivity::class.java))
//            finish()
//        }
//
//        leaderboardBtn.setOnClickListener {
//            val intent = Intent(this, LeaderboardActivity::class.java)
//            intent.putExtra("username", username)
//            startActivity(intent)
//        }
//
//        logoutBtn.setOnClickListener {
//            prefs.edit().remove("username").apply()
//            val intent = Intent(this, WelcomeActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//            startActivity(intent)
//            finish()
//        }
//    }
//}
package com.example.aplicatie

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.aplicatie.data.UserRepository
import com.example.aplicatie.ui.auth.WelcomeActivity
import com.example.aplicatie.ui.leaderboard.LeaderboardActivity

class ResultActivity : AppCompatActivity() {
    private val repo = UserRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val score = intent.getIntExtra("score", 0)

        val prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val username = prefs.getString("username", null)

        findViewById<TextView>(R.id.result_text).text = "Scor final: $score"

        if (username != null) {
            repo.getHighScore(username) { highScore ->
                if (highScore == null) {
                    findViewById<TextView>(R.id.high_score_text).text = "Scor maxim: necunoscut"
                } else {
                    if (score > highScore) {
                        repo.updateHighScore(username, score)
                        findViewById<TextView>(R.id.high_score_text).text = "Scor maxim: $score"
                    } else {
                        findViewById<TextView>(R.id.high_score_text).text = "Scor maxim: $highScore"
                    }
                }
            }
        }

        findViewById<Button>(R.id.play_again_button).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        findViewById<Button>(R.id.view_leaderboard_button).setOnClickListener {
            val i = Intent(this, LeaderboardActivity::class.java)
            i.putExtra("username", username)
            startActivity(i)
        }

        findViewById<Button>(R.id.logout_button).setOnClickListener {
            prefs.edit().remove("username").apply()
            val i = Intent(this, WelcomeActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(i)
        }
    }
}

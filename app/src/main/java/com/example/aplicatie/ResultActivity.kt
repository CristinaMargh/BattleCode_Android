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
        val wrongQuestions = intent.getStringArrayListExtra("wrongQuestions") ?: arrayListOf()
        val wrongCorrectAnswers = intent.getStringArrayListExtra("wrongCorrectAnswers") ?: arrayListOf()
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
        // buton: See Wrong Questions
        findViewById<Button>(R.id.see_wrong_button).setOnClickListener {
            val wrongQuestions = intent.getStringArrayListExtra("wrongQuestions") ?: arrayListOf()
            val i = Intent(this, com.example.aplicatie.ui.WrongAnswersActivity::class.java)
            i.putStringArrayListExtra("wrongQuestions", wrongQuestions)
            i.putStringArrayListExtra("wrongCorrectAnswers", wrongCorrectAnswers)
            startActivity(i)
        }
    }
}

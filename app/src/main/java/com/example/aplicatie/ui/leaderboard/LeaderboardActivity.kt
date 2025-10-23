package com.example.aplicatie.ui.leaderboard

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.aplicatie.MainActivity
import com.example.aplicatie.R
import com.example.aplicatie.data.User
import com.example.aplicatie.data.UserRepository
import com.example.aplicatie.ui.auth.WelcomeActivity

class LeaderboardActivity : AppCompatActivity() {
    private val repo = UserRepository()
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var currentUser: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
        currentUser = intent.getStringExtra("username") ?: prefs.getString("username", "???") ?: "???"

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(32, 64, 32, 64)
            setBackgroundResource(R.drawable.background_gradient)
        }

        val title = TextView(this).apply {
            text = "🏆 Clasament Jucători 🏆"
            textSize = 24f
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
            setPadding(0, 0, 0, 24)
        }
        layout.addView(title)

        val list = ListView(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                0,
                1f
            )
            id = R.id.leaderboard_list
        }
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, mutableListOf())
        list.adapter = adapter
        layout.addView(list)

        val buttonParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            setMargins(0, 16, 0, 0)
        }

        val returnBtn = Button(this).apply {
            text = "RETURN TO MENU"
            setTextColor(Color.WHITE)
            textSize = 18f
            background = getDrawable(R.drawable.rounded_button)
            layoutParams = buttonParams
            setOnClickListener {
                startActivity(Intent(this@LeaderboardActivity, MainActivity::class.java))
                finish()
            }
        }

        val logoutBtn = Button(this).apply {
            text = "LOGOUT"
            setTextColor(Color.WHITE)
            textSize = 18f
            background = getDrawable(R.drawable.rounded_button)
            layoutParams = buttonParams
            setOnClickListener {
                prefs.edit().remove("username").apply()
                val i = Intent(this@LeaderboardActivity, WelcomeActivity::class.java)
                i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(i)
                finish()
            }
        }

        layout.addView(returnBtn)
        layout.addView(logoutBtn)

        setContentView(layout)
        loadBoard()
    }

    private fun loadBoard() {
        repo.loadLeaderboard(
            onLoaded = { users -> show(users) },
            onError = { Toast.makeText(this, it, Toast.LENGTH_SHORT).show() }
        )
    }

    private fun show(users: List<User>) {
        val rows = users.mapIndexed { i, u ->
            val marker = if (u.username == currentUser) " ← you" else ""
            "${i + 1}.  ${u.username} — ${u.highScore}$marker"
        }

        adapter.clear()
        adapter.addAll(rows)

        val listView = findViewById<ListView>(R.id.leaderboard_list)
        listView.adapter = object : ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, rows) {
            override fun getView(position: Int, convertView: android.view.View?, parent: android.view.ViewGroup): android.view.View {
                val view = super.getView(position, convertView, parent) as TextView
                view.setTextColor(Color.WHITE)
                view.textSize = 20f
                view.setPadding(20, 20, 20, 20)
                view.setTypeface(null, android.graphics.Typeface.BOLD)
                return view
            }
        }
    }
}

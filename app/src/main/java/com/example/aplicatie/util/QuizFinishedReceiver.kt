package com.example.aplicatie.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class QuizFinishedReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == ACTION_QUIZ_FINISHED) {
            val username = intent.getStringExtra("username") ?: "ANONIM"
            val score = intent.getIntExtra("score", 0)

            Toast.makeText(
                context,
                "Broadcast: $username just finished a quiz with score $score",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    companion object {
        const val ACTION_QUIZ_FINISHED =
            "com.example.aplicatie.ACTION_QUIZ_FINISHED"
    }
}

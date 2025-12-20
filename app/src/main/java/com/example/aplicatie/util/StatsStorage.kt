package com.example.aplicatie.util

import android.content.Context
import com.example.aplicatie.data.QuizStats
import com.google.gson.Gson

object StatsStorage {
    private const val PREFS = "stats_prefs"
    private const val KEY_STATS = "stats_json"
    private val gson = Gson()

    fun load(context: Context, username: String): QuizStats {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val json = prefs.getString("$KEY_STATS-$username", null)
        return if (json == null) {
            QuizStats()
        } else {
            gson.fromJson(json, QuizStats::class.java)
        }
    }

    fun save(context: Context, username: String, stats: QuizStats) {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val json = gson.toJson(stats)
        prefs.edit().putString("$KEY_STATS-$username", json).apply()
    }
}

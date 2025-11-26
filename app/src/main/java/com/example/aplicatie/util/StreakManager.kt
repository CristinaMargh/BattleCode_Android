package com.example.aplicatie.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

object StreakManager {

    private const val PREFS_PREFIX = "streak_"        // vom avea streak_<username>
    private const val KEY_LAST_DAY = "last_day"       // YYYYMMDD
    private const val KEY_STREAK = "streak"
    private const val KEY_LAST_TS = "last_ts"         // millis

    const val REMINDER_WORK = "streak_reminder"

    // ------------ helpers pentru preferințele unui user -----------------

    private fun prefsForUser(ctx: Context, username: String?): android.content.SharedPreferences {
        // dacă nu avem username, folosim un fallback generic
        val name = if (username.isNullOrBlank()) "${PREFS_PREFIX}default" else PREFS_PREFIX + username
        return ctx.getSharedPreferences(name, Context.MODE_PRIVATE)
    }

    // yyyyMMdd (ex: 20250319)
    private fun todayInt(): Int {
        val now = java.util.Calendar.getInstance()
        val y = now.get(java.util.Calendar.YEAR)
        val m = now.get(java.util.Calendar.MONTH) + 1
        val d = now.get(java.util.Calendar.DAY_OF_MONTH)
        return y * 10_000 + m * 100 + d
    }

    // ------------ apelat când userul termină un quiz --------------------

    fun onQuizFinished(context: Context, username: String?) {
        val prefs = prefsForUser(context, username)

        val today = todayInt()
        val lastDay = prefs.getInt(KEY_LAST_DAY, 0)
        var streak = prefs.getInt(KEY_STREAK, 0)

        if (lastDay == today) {
            // deja a făcut un quiz azi, nu modificăm streak-ul, dar updatăm timestamp-ul
        } else if (lastDay == today - 1) {
            // zi consecutivă -> creștem streak
            streak += 1
        } else {
            // pauză mai mare de o zi -> reset
            streak = 1
        }

        val now = System.currentTimeMillis()

        prefs.edit()
            .putInt(KEY_LAST_DAY, today)
            .putInt(KEY_STREAK, streak)
            .putLong(KEY_LAST_TS, now)
            .apply()

        // re-programează reminderul la ~20h după ultimul quiz
        scheduleReminder(context)
    }

    // streak curent pentru profil
    fun getCurrentStreak(context: Context): Int {
        val appPrefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val username = appPrefs.getString("username", null)
        val prefs = prefsForUser(context, username)
        return prefs.getInt(KEY_STREAK, 0)
    }

    // ------------ WorkManager: programare reminder ----------------------

    private fun scheduleReminder(context: Context) {
        val work = OneTimeWorkRequestBuilder<StreakReminderWorker>()
            .setInitialDelay(20, TimeUnit.HOURS)   // rulează după ~20h
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            REMINDER_WORK,
            ExistingWorkPolicy.REPLACE,
            work
        )
    }

    // apelată DIN worker → decide dacă trimite notificarea
    fun maybeShowReminder(ctx: Context) {
        val appPrefs = ctx.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val username = appPrefs.getString("username", null)
        val prefs = prefsForUser(ctx, username)

        val lastTs = prefs.getLong(KEY_LAST_TS, 0L)
        if (lastTs == 0L) return

        val now = System.currentTimeMillis()
        val diff = now - lastTs

        val twentyHours = TimeUnit.HOURS.toMillis(20)
        val oneDay = TimeUnit.HOURS.toMillis(24)

        // trimitem notificare doar dacă suntem între 20 și 24h de la ultimul quiz
        if (diff in twentyHours..oneDay) {
            showNotification(ctx)
        }
    }

    // ------------ notificarea propriu-zisă ------------------------------

    private fun showNotification(ctx: Context) {
        val channelId = "streak_channel"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val ch = NotificationChannel(
                channelId,
                "BattleCode streak",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val nm = ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            nm.createNotificationChannel(ch)
        }

        val notif = NotificationCompat.Builder(ctx, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Don't lose your streak!")
            .setContentText("You have about 4 hours left to complete a BattleCode quiz.")
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(ctx).notify(1001, notif)
    }
}

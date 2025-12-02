package com.example.aplicatie.util

import android.Manifest
import android.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.content.edit // Extensia KTX pentru SharedPreferences
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.concurrent.TimeUnit

object StreakManager {

    // Constante pentru WorkManager și Notificare
    private const val PREFS_PREFIX = "streak_"
    private const val KEY_LAST_DAY = "last_day"
    private const val KEY_STREAK = "streak"
    private const val KEY_LAST_TS = "last_ts"
    const val REMINDER_WORK = "streak_reminder"

    // Constante pentru Canalul de Notificare
    private const val CHANNEL_ID = "streak_reminder_channel"
    private const val NOTIFICATION_ID = 1001

    /**
     * @param lastQuizDay      ultima zi în care userul a făcut quiz (sau null dacă nu are streak)
     * @param today            ziua curentă
     * @param previousStreak   streak-ul anterior (0 dacă nu avea)
     *
     * Reguli:
     * - Functia este corectă, dar este nefolosită în codul furnizat.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun computeNewStreak(
        lastQuizDay: LocalDate?,
        today: LocalDate,
        previousStreak: Int
    ): Int {
        if (lastQuizDay == null) return 1

        val diff = ChronoUnit.DAYS.between(lastQuizDay, today)

        return when {
            diff <= 0L -> previousStreak          // aceeași zi sau „în trecut”
            diff == 1L -> previousStreak + 1      // zi consecutivă
            else -> 1                              // streak reset
        }
    }

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

        if (lastDay != today) {
            streak = if (lastDay == today - 1) {
                // zi consecutivă -> creștem streak
                streak + 1
            } else {
                // pauză mai mare de o zi -> reset
                1
            }
        }
        // Dacă lastDay == today, streak rămâne la fel (dar updatăm doar timestamp-ul)

        val now = System.currentTimeMillis()

        // Folosirea extensiei KTX `edit { ... }` - mai curat decât `.edit().apply()`
        prefs.edit {
            putInt(KEY_LAST_DAY, today)
            putInt(KEY_STREAK, streak)
            putLong(KEY_LAST_TS, now)
        }

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
            // Aici trebuie să ne asigurăm că permisiunea a fost acordată
            showNotificationIfPermissionGranted(ctx)
        }
    }

    // ------------ notificarea propriu-zisă ------------------------------

    /**
     * Functia care trimite notificarea, DACA permisiunea a fost deja acordată.
     * Pe Android 13+ (API 33+) această permisiune trebuie cerută de la utilizator.
     */
    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    private fun showNotification(ctx: Context) {
        // 1. Creează canalul de notificare (Obligatoriu pentru Android 8.0+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val ch = NotificationChannel(
                CHANNEL_ID,
                "BattleCode streak",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val nm = ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            nm.createNotificationChannel(ch)
        }

        // 2. Construiește notificarea
        val notif = NotificationCompat.Builder(ctx, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_dialog_info)
            .setContentTitle("Don't lose your streak!")
            .setContentText("You have about 4 hours left to complete a BattleCode quiz.")
            .setAutoCancel(true)
            .build()

        // 3. Afișează notificarea
        NotificationManagerCompat.from(ctx).notify(NOTIFICATION_ID, notif)
    }

    /**
     * Punctul de intrare pentru afișarea notificării, care verifică permisiunea.
     */
    private fun showNotificationIfPermissionGranted(ctx: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13+ necesită permisiunea POST_NOTIFICATIONS
            if (ContextCompat.checkSelfPermission(
                    ctx,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // Dacă permisiunea nu e acordată, NU putem afișa notificarea.
                // În contextul unui Worker, nu putem cere permisiunea, doar în Activity/Fragment.
                // Prin urmare, o omitem.
                return
            }
        }

        showNotification(ctx)
    }
}
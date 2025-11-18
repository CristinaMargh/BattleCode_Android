package com.example.aplicatie.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.*
import java.util.concurrent.TimeUnit
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat



object StreakManager {

    private const val PREFS = "streak_prefs"
    private const val KEY_LAST_DAY = "last_day"          // YYYYMMDD
    private const val KEY_STREAK = "streak"
    private const val KEY_LAST_TS = "last_ts"            // millis
    private const val REMINDER_WORK = "streak_reminder"

    /**
     * Apeși asta când utilizatorul termină un quiz.
     * Actualizează streak-ul + programează reminderul de 20h.
     */
    fun onQuizFinished(ctx: Context) {
        val prefs = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

        val now = System.currentTimeMillis()
        val today = dayCode(now)
        val lastDay = prefs.getInt(KEY_LAST_DAY, -1)
        val oldStreak = prefs.getInt(KEY_STREAK, 0)

        val newStreak = when {
            lastDay == today -> oldStreak              // deja a jucat azi
            lastDay == today - 1 -> oldStreak + 1      // ieri + azi -> streak++
            else -> 1                                   // pauză mai mare => reset
        }

        prefs.edit()
            .putInt(KEY_LAST_DAY, today)
            .putInt(KEY_STREAK, newStreak)
            .putLong(KEY_LAST_TS, now)
            .apply()

        scheduleReminder(ctx, now)
    }

    fun getCurrentStreak(ctx: Context): Int =
        ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getInt(KEY_STREAK, 0)

    /** Stochează în prefs o limbă mai explicită – doar dacă vrei s-o afișezi */
    private fun dayCode(millis: Long): Int {
        // transformăm în "YYYYMMDD" simplu
        val cal = java.util.Calendar.getInstance().apply { timeInMillis = millis }
        val y = cal.get(java.util.Calendar.YEAR)
        val m = cal.get(java.util.Calendar.MONTH) + 1
        val d = cal.get(java.util.Calendar.DAY_OF_MONTH)
        return y * 10_000 + m * 100 + d
    }

    /**
     * Programează un worker care rulează după 20 de ore
     * (24h - 4h => notificarea „pierzi streak-ul în 4 ore”).
     *
     * În timpul dezvoltării poți schimba 20 de ore cu 1 minut.
     */
    fun scheduleReminder(ctx: Context, fromTs: Long) {
        // anulăm reminder-ul vechi, dacă exista
        WorkManager.getInstance(ctx).cancelUniqueWork(REMINDER_WORK)

        val delayMillis = TimeUnit.HOURS.toMillis(20)   // PROD
         //val delayMillis = TimeUnit.MINUTES.toMillis(1)   // TEST

        val request = OneTimeWorkRequestBuilder<StreakReminderWorker>()
            .setInitialDelay(delayMillis, TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance(ctx)
            .enqueueUniqueWork(REMINDER_WORK, ExistingWorkPolicy.REPLACE, request)
    }

    /**
     * Verifică dacă încă NU a mai fost jucat un quiz de când am programat reminderul.
     * Dacă nu, trimite notificarea de „mai ai 4 ore”.
     */
    internal fun maybeShowReminder(ctx: Context) {
        val prefs = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val lastTs = prefs.getLong(KEY_LAST_TS, -1L)
        if (lastTs <= 0L) return

        val now = System.currentTimeMillis()
        val diff = now - lastTs

        // suntem între 20h și 24h de la ultimul quiz => trimitem notificare
        val twentyH = TimeUnit.HOURS.toMillis(20)
        val twentyFourH = TimeUnit.HOURS.toMillis(24)

        if (diff in twentyH..twentyFourH) {
            showNotification(ctx)
        }
        // dacă userul tot nu joacă, streak-ul va fi resetat data viitoare când chemi onQuizFinished
    }

    private fun showNotification(ctx: Context) {
        // Android 13+ – check POST_NOTIFICATIONS
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(
                    ctx,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // nu avem permisiunea -> ieșim fără să dăm notify
                return
            }
        }

        val channelId = "streak_channel"

        // channel (doar o dată)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val ch = NotificationChannel(
                channelId,
                "Streak reminders",
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

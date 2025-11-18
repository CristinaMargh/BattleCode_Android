package com.example.aplicatie.util

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class StreakReminderWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        // verifică dacă chiar trebuie trimisă notificarea
        StreakManager.maybeShowReminder(applicationContext)
        return Result.success()
    }
}

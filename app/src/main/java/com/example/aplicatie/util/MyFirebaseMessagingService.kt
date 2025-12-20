package com.example.aplicatie.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.content.Context.MODE_PRIVATE
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.aplicatie.MainActivity
import com.example.aplicatie.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "New FCM token: $token")

        // Optional: store token under the last known username (if you have it).
        // In this project we store token on login/register via PushTokenManager,
        // but keeping this is useful if token rotates.
        val prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val username = prefs.getString("username", null)
        if (!username.isNullOrBlank()) {
            storeTokenInFirestore(username, token)
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val title = message.notification?.title ?: message.data["title"] ?: "BattleCode"
        val body = message.notification?.body ?: message.data["body"] ?: "Ai o notificare nouă."

        Log.d(TAG, "Message received. title=$title body=$body data=${message.data}")

        ensureChannel()
        showNotification(title, body)
    }

    private fun storeTokenInFirestore(username: String, token: String) {
        val db = FirebaseFirestore.getInstance()

        val payload = mapOf(
            "fcmToken" to token,
            "updatedAt" to com.google.firebase.Timestamp.now()
        )

        db.collection("users")
            .document(username)
            .set(payload, SetOptions.merge())
            .addOnSuccessListener { Log.d(TAG, "Token stored for user=$username") }
            .addOnFailureListener { e -> Log.w(TAG, "Failed to store token for user=$username", e) }
    }

    private fun ensureChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "BattleCode notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Push notifications for BattleCode"
            }

            val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            nm.createNotificationChannel(channel)
        }
    }

    private fun showNotification(title: String, body: String) {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
        )

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(body)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        val nm = NotificationManagerCompat.from(this)

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            val granted = androidx.core.content.ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED

            if (!granted) {
                Log.w(TAG, "POST_NOTIFICATIONS not granted; skipping notification")
                return
            }
        }

        nm.notify(NOTIF_ID, notification)

    }

    companion object {
        private const val TAG = "MyFCMService"
        private const val CHANNEL_ID = "fcm_default_channel"
        private const val NOTIF_ID = 2001
    }
}

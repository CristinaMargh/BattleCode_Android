package com.example.aplicatie.util

import android.content.Context
import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.messaging.FirebaseMessaging

object PushTokenManager {

    private const val TAG = "PushTokenManager"
    private const val PREFS = "battlecode_prefs"
    private const val KEY_USERNAME = "current_username"

    /**
     * 1) ia token-ul FCM curent
     * 2) salvează username în SharedPreferences (ca onNewToken să știe unde să scrie)
     * 3) scrie token-ul în Firestore: users/{username}.fcmToken
     * 4) (opțional) subscribe la topic "all"
     */
    fun refreshAndStoreToken(context: Context, username: String) {
        // keep username for future token rotations
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_USERNAME, username)
            .apply()

        FirebaseMessaging.getInstance().token
            .addOnSuccessListener { token ->
                Log.d(TAG, "FCM token=$token for user=$username")
                storeTokenInFirestore(username, token)
                subscribeToAllTopic()
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Failed to get FCM token", e)
            }
    }

    private fun storeTokenInFirestore(username: String, token: String) {
        val db = FirebaseFirestore.getInstance()

        val payload = mapOf(
            "fcmToken" to token,
            "updatedAt" to Timestamp.now()
        )

        db.collection("users")
            .document(username)
            .set(payload, SetOptions.merge())
            .addOnSuccessListener { Log.d(TAG, "Token stored in Firestore for $username") }
            .addOnFailureListener { e -> Log.w(TAG, "Failed to store token in Firestore", e) }
    }

    private fun subscribeToAllTopic() {
        FirebaseMessaging.getInstance()
            .subscribeToTopic("all")
            .addOnSuccessListener { Log.d(TAG, "Subscribed to topic: all") }
            .addOnFailureListener { e -> Log.w(TAG, "Failed subscribing to topic: all", e) }
    }
}

package com.example.aplicatie.data

import com.google.firebase.firestore.FirebaseFirestore
import java.security.MessageDigest

class UserRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    private val users = db.collection("users")

    private fun sha256(text: String): String =
        MessageDigest.getInstance("SHA-256")
            .digest(text.toByteArray())
            .joinToString("") { "%02x".format(it) }

    fun register(
        username: String,
        password: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        users.document(username).get().addOnSuccessListener { snap ->
            if (snap.exists()) {
                onResult(false, "Username already taken")
            } else {
                val user = User(username, sha256(password), 0)
                users.document(username).set(user)
                    .addOnSuccessListener { onResult(true, null) }
                    .addOnFailureListener { onResult(false, it.message) }
            }
        }.addOnFailureListener { onResult(false, it.message) }
    }

    fun login(
        username: String,
        password: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        users.document(username).get().addOnSuccessListener { snap ->
            if (!snap.exists()) { onResult(false, "User not found"); return@addOnSuccessListener }
            val stored = snap.toObject(User::class.java)!!
            if (stored.passwordHash == sha256(password))
                onResult(true, null)
            else onResult(false, "Wrong password")
        }.addOnFailureListener { onResult(false, it.message) }
    }

    fun loadLeaderboard(
        onLoaded: (List<User>) -> Unit,
        onError: (String) -> Unit
    ) {
        users.orderBy("highScore", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { qs ->
                onLoaded(qs.toObjects(User::class.java))
            }
            .addOnFailureListener { onError(it.message ?: "DB error") }
    }
    fun updateHighScore(username: String, score: Int) {
        users.document(username).get().addOnSuccessListener { doc ->
            val current = doc.getLong("highScore")?.toInt() ?: 0
            if (score > current) {
                users.document(username).update("highScore", score)
            }
        }
    }

    fun getHighScore(username: String, callback: (Int?) -> Unit) {
        users.document(username).get().addOnSuccessListener { doc ->
            val hs = doc.getLong("highScore")?.toInt()
            callback(hs)
        }.addOnFailureListener {
            callback(null)
        }
    }

}

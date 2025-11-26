package com.example.aplicatie.data

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import java.security.MessageDigest
import com.google.firebase.firestore.SetOptions

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
    fun updateAfterQuiz(
        username: String,
        lastScore: Int,
        correctCount: Int,
        totalQuestions: Int
    ) {
        val ref = users.document(username)

        db.runTransaction { tr ->
            val snap = tr.get(ref)

            val currentHigh       = snap.getLong("highScore")      ?: 0L
            val currentTotalQz    = snap.getLong("totalQuizzes")   ?: 0L
            val currentTotalQs    = snap.getLong("totalQuestions") ?: 0L
            val currentTotalCorr  = snap.getLong("totalCorrect")   ?: 0L

            val newHigh        = maxOf(currentHigh, lastScore.toLong())
            val newTotalQz     = currentTotalQz + 1
            val newTotalQs     = currentTotalQs + totalQuestions
            val newTotalCorr   = currentTotalCorr + correctCount

            val data = mapOf(
                "highScore"      to newHigh,
                "lastScore"      to lastScore,
                "totalQuizzes"   to newTotalQz,
                "totalQuestions" to newTotalQs,
                "totalCorrect"   to newTotalCorr
            )

            // merge = păstrează câmpurile existente (username, passwordHash etc.)
            tr.set(ref, data, SetOptions.merge())
        }
    }
    fun addFriend(
        currentUsername: String,
        friendUsername: String,
        callback: (Boolean, String?) -> Unit
    ) {
        if (friendUsername == currentUsername) {
            callback(false, "You can't add yourself")
            return
        }

        // 1. verificăm dacă există userul
        users.document(friendUsername).get()
            .addOnSuccessListener { snap ->
                if (!snap.exists()) {
                    callback(false, "User not found")
                    return@addOnSuccessListener
                }

                // 2. facem update pe doc-ul userului curent: arrayUnion
                users.document(currentUsername)
                    .update("friends", FieldValue.arrayUnion(friendUsername))
                    .addOnSuccessListener { callback(true, null) }
                    .addOnFailureListener { e -> callback(false, e.message) }
            }
            .addOnFailureListener { e ->
                callback(false, e.message)
            }
    }

    /**
     * Load list of friends as full User objects (pentru listă în UI).
     */
    fun getFriends(
        currentUsername: String,
        onResult: (List<User>) -> Unit,
        onError: (String) -> Unit
    ) {
        users.document(currentUsername).get()
            .addOnSuccessListener { doc ->
                val friendNames = doc.get("friends") as? List<String> ?: emptyList()
                if (friendNames.isEmpty()) {
                    onResult(emptyList())
                    return@addOnSuccessListener
                }

                // whereIn suportă max 10 elemente, dar pt proiectul tău e super ok.
                users.whereIn("username", friendNames)
                    .get()
                    .addOnSuccessListener { qs ->
                        val list = qs.toObjects(User::class.java)
                        onResult(list)
                    }
                    .addOnFailureListener { e ->
                        onError(e.message ?: "DB error")
                    }
            }
            .addOnFailureListener { e ->
                onError(e.message ?: "DB error")
            }
    }

}

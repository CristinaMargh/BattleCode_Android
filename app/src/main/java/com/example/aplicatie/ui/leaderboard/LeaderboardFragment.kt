package com.example.aplicatie.ui.leaderboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.aplicatie.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class LeaderboardFragment : Fragment() {

    private val db by lazy { FirebaseFirestore.getInstance() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_leaderboard, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tv = view.findViewById<TextView>(R.id.lb_text)
        tv.text = "Loading winner..."

        // Ajustează numele colecției câmpurile după cum le ai în Firestore
        db.collection("users")
            .orderBy("highScore", Query.Direction.DESCENDING)
            .limit(1)
            .get()
            .addOnSuccessListener { snap ->
                if (!isAdded) return@addOnSuccessListener
                if (!snap.isEmpty) {
                    val doc = snap.documents.first()
                    val name = doc.getString("username") ?: "Unknown"
                    val score = doc.getLong("highScore") ?: 0
                    tv.text = "Current winner: $name ($score)"
                } else {
                    tv.text = "No scores yet"
                }
            }
            .addOnFailureListener {
                if (!isAdded) return@addOnFailureListener
                tv.text = "Failed to load winner"
            }
    }
}

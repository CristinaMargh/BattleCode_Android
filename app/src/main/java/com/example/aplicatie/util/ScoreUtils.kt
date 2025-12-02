package com.example.aplicatie.util

object ScoreUtils {
    /**
     * Same logic as in QuizActivity:
     * score = (maxTimeMs - timeTakenMs) / 1000
     * Clamped so it never goes negative.
     */
    fun calculateScore(timeTakenMs: Long, maxTimeMs: Long = 10_000L): Int {
        val raw = ((maxTimeMs - timeTakenMs).toInt()) / 1000
        return if (raw < 0) 0 else raw
    }
}

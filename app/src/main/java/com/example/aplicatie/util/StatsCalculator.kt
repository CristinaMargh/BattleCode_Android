package com.example.aplicatie.util

data class QuizSession(
    val correctAnswers: Int,
    val totalQuestions: Int,
    val score: Int
)

data class UserStats(
    val totalQuizzes: Int,
    val totalQuestions: Int,
    val totalCorrect: Int,
    val bestScore: Int,
    val lastScore: Int,
    val accuracyPercent: Double
)

object StatsCalculator {

    fun computeStats(sessions: List<QuizSession>): UserStats {
        if (sessions.isEmpty()) {
            return UserStats(
                totalQuizzes = 0,
                totalQuestions = 0,
                totalCorrect = 0,
                bestScore = 0,
                lastScore = 0,
                accuracyPercent = 0.0
            )
        }

        val totalQ = sessions.sumOf { it.totalQuestions }
        val totalC = sessions.sumOf { it.correctAnswers }
        val best = sessions.maxOf { it.score }
        val last = sessions.last().score
        val acc = if (totalQ == 0) 0.0 else (totalC.toDouble() / totalQ.toDouble()) * 100.0

        return UserStats(
            totalQuizzes = sessions.size,
            totalQuestions = totalQ,
            totalCorrect = totalC,
            bestScore = best,
            lastScore = last,
            accuracyPercent = acc
        )
    }
}

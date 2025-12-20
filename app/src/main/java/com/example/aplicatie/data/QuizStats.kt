package com.example.aplicatie.data

data class QuizStats(
    val totalQuizzes: Int = 0,
    val lastScore: Int = 0,
    val bestScore: Int = 0,
    val totalAnswers: Int = 0,
    val correctAnswers: Int = 0
) {
    val accuracy: Double
        get() = if (totalAnswers == 0) 0.0 else correctAnswers.toDouble() / totalAnswers
}

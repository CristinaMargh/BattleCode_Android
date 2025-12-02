package com.example.aplicatie.util

import org.junit.Assert.assertEquals
import org.junit.Test

class StatsCalculatorTest {

    @Test
    fun `empty list returns zero stats`() {
        val stats = StatsCalculator.computeStats(emptyList())

        assertEquals(0, stats.totalQuizzes)
        assertEquals(0, stats.totalQuestions)
        assertEquals(0, stats.totalCorrect)
        assertEquals(0, stats.bestScore)
        assertEquals(0, stats.lastScore)
        assertEquals(0.0, stats.accuracyPercent, 0.0001)
    }

    @Test
    fun `single session computed correctly`() {
        val sessions = listOf(
            QuizSession(correctAnswers = 4, totalQuestions = 5, score = 70)
        )

        val stats = StatsCalculator.computeStats(sessions)

        assertEquals(1, stats.totalQuizzes)
        assertEquals(5, stats.totalQuestions)
        assertEquals(4, stats.totalCorrect)
        assertEquals(70, stats.bestScore)
        assertEquals(70, stats.lastScore)
        assertEquals(80.0, stats.accuracyPercent, 0.0001)
    }

    @Test
    fun `multiple sessions aggregate properly`() {
        val sessions = listOf(
            QuizSession(correctAnswers = 3, totalQuestions = 5, score = 60),
            QuizSession(correctAnswers = 5, totalQuestions = 5, score = 100),
            QuizSession(correctAnswers = 4, totalQuestions = 5, score = 80)
        )

        val stats = StatsCalculator.computeStats(sessions)

        assertEquals(3, stats.totalQuizzes)
        assertEquals(15, stats.totalQuestions)
        assertEquals(12, stats.totalCorrect)         // 3+5+4
        assertEquals(100, stats.bestScore)          // max score
        assertEquals(80, stats.lastScore)           // ultimul
        assertEquals(80.0, stats.accuracyPercent, 0.0001) // 12/15 * 100 = 80
    }
}

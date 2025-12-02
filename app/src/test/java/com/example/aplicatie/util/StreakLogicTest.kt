package com.example.aplicatie

import com.example.aplicatie.util.StreakManager
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class StreakLogicTest {

    @Test
    fun firstQuiz_startsAtOne() {
        val today = LocalDate.of(2025, 12, 1)
        val newStreak = StreakManager.computeNewStreak(
            lastQuizDay = null,
            today = today,
            previousStreak = 0
        )
        assertEquals(1, newStreak)
    }

    @Test
    fun sameDay_quiz_doesNotIncreaseStreak() {
        val day = LocalDate.of(2025, 12, 1)
        val newStreak = StreakManager.computeNewStreak(
            lastQuizDay = day,
            today = day,
            previousStreak = 3
        )
        assertEquals(3, newStreak)
    }

    @Test
    fun nextDay_increasesStreak() {
        val yesterday = LocalDate.of(2025, 11, 30)
        val today = LocalDate.of(2025, 12, 1)

        val newStreak = StreakManager.computeNewStreak(
            lastQuizDay = yesterday,
            today = today,
            previousStreak = 2
        )
        assertEquals(3, newStreak)
    }

    @Test
    fun gapOfTwoDays_resetsStreak() {
        val last = LocalDate.of(2025, 11, 28)
        val today = LocalDate.of(2025, 11, 30)

        val newStreak = StreakManager.computeNewStreak(
            lastQuizDay = last,
            today = today,
            previousStreak = 5
        )
        assertEquals(1, newStreak)
    }
}

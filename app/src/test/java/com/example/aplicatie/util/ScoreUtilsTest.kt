package com.example.aplicatie

import com.example.aplicatie.util.ScoreUtils
import org.junit.Assert.assertEquals
import org.junit.Test

class ScoreUtilsTest {

    @Test
    fun fastAnswer_givesHighScore() {
        // 1s -> aproape maximum
        val score = ScoreUtils.calculateScore(timeTakenMs = 1_000L)
        assertEquals(9, score)   // (10000 - 1000) / 1000 = 9
    }

    @Test
    fun slowAnswer_givesLowScore() {
        // 9s -> scor mic dar >0
        val score = ScoreUtils.calculateScore(timeTakenMs = 9_000L)
        assertEquals(1, score)   // (10000 - 9000)/1000 = 1
    }

    @Test
    fun tooSlow_clampsToZero() {
        // 20s -> ar da negativ, dar noi îl tăiem la 0
        val score = ScoreUtils.calculateScore(timeTakenMs = 20_000L)
        assertEquals(0, score)
    }

    @Test
    fun instantAnswer_almostMaxScore() {
        val score = ScoreUtils.calculateScore(timeTakenMs = 0L)
        assertEquals(10, score)  // 10000 / 1000
    }
}

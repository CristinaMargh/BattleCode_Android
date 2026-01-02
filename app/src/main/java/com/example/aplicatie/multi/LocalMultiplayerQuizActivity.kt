package com.example.aplicatie.ui.multi

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.aplicatie.ui.theme.AplicatieTheme
import kotlinx.coroutines.delay

class LocalMultiplayerQuizActivity : ComponentActivity() {

    data class Q(val text: String, val options: List<String>, val correctAnswerIndex: Int)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val username = intent.getStringExtra("username") ?: "ANONIM"
        val topic = intent.getStringExtra("topic") ?: "cpp"
        val difficulty = intent.getStringExtra("difficulty") ?: "easy"

        // momentan întrebări demo (ca să meargă imediat)
        val questions = sampleQuestions().shuffled().take(5)

        setContent {
            AplicatieTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    LocalMultiplayerQuizScreen(
                        questions = questions,
                        onFinish = { p1, p2 ->
                            startActivity(
                                Intent(this, MultiplayerResultActivity::class.java).apply {
                                    putExtra("p1Score", p1)
                                    putExtra("p2Score", p2)
                                    putExtra("username", username)
                                    putExtra("topic", topic)
                                    putExtra("difficulty", difficulty)
                                }
                            )
                            finish()
                        }
                    )
                }
            }
        }
    }

    private fun sampleQuestions(): List<Q> = listOf(
        Q("C++: Which header for cout?", listOf("<iostream>", "<vector>", "<string>", "<stdio.h>"), 0),
        Q("Java: Entry point method?", listOf("start()", "run()", "main()", "init()"), 2),
        Q("Python: Comment symbol?", listOf("#", "//", "/* */", "--"), 0),
        Q("C++: Assignment operator?", listOf("==", "=", ":=", "->"), 1),
        Q("Java: Keyword to create object?", listOf("new", "make", "alloc", "create"), 0),
        Q("Python: exponent operator?", listOf("^", "**", "pow", "exp"), 1)
    )
}

@Composable
private fun LocalMultiplayerQuizScreen(
    questions: List<LocalMultiplayerQuizActivity.Q>,
    onFinish: (p1Score: Int, p2Score: Int) -> Unit
) {
    val players = listOf("Player 1", "Player 2")

    var questionIndex by remember { mutableIntStateOf(0) }
    var playerTurn by remember { mutableIntStateOf(0) } // 0 -> P1, 1 -> P2
    var p1Score by remember { mutableIntStateOf(0) }
    var p2Score by remember { mutableIntStateOf(0) }

    var timeLeft by remember { mutableIntStateOf(10) }
    var answered by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableIntStateOf(-1) }

    val currentQ = questions.getOrNull(questionIndex)

    // Timer (se oprește când answered = true)
    LaunchedEffect(questionIndex, playerTurn, answered) {
        if (currentQ == null) return@LaunchedEffect
        timeLeft = 10
        if (answered) return@LaunchedEffect

        while (timeLeft > 0 && !answered) {
            delay(1000)
            timeLeft -= 1
        }

        // timeout
        if (!answered) {
            answered = true
            selectedIndex = -1
        }
    }

    if (currentQ == null) {
        onFinish(p1Score, p2Score)
        return
    }

    fun scoreForTimeLeft(secondsLeft: Int): Int {
        // similar cu calculateScore: (10_000 - timeTaken)/1000
        // aici: dacă mai ai 10s => 10p, dacă mai ai 1s => 1p
        return secondsLeft.coerceIn(0, 10)
    }

    fun goNext() {
        answered = false
        selectedIndex = -1

        if (playerTurn == 0) {
            playerTurn = 1
        } else {
            playerTurn = 0
            questionIndex += 1
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "${players[playerTurn]} turn",
            style = MaterialTheme.typography.titleLarge
        )

        Text(
            text = "Time left: ${timeLeft}s",
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = currentQ.text,
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(Modifier.height(6.dp))

        currentQ.options.forEachIndexed { idx, opt ->
            val isCorrect = idx == currentQ.correctAnswerIndex
            val isSelected = idx == selectedIndex

            val buttonColors = when {
                !answered -> ButtonDefaults.buttonColors()
                // după răspuns/timeout: corectul devine verde (implicit), greșitul rămâne “selected”
                answered && isCorrect -> ButtonDefaults.buttonColors()
                answered && isSelected && !isCorrect -> ButtonDefaults.buttonColors()
                else -> ButtonDefaults.buttonColors()
            }

            Button(
                onClick = {
                    if (answered) return@Button

                    selectedIndex = idx
                    answered = true

                    if (idx == currentQ.correctAnswerIndex) {
                        val gained = scoreForTimeLeft(timeLeft)
                        if (playerTurn == 0) p1Score += gained else p2Score += gained
                    }
                },
                enabled = !answered,
                colors = buttonColors,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(opt)
            }
        }

        Spacer(Modifier.height(8.dp))

        Text(
            text = "Score: P1=$p1Score | P2=$p2Score",
            style = MaterialTheme.typography.titleMedium
        )

        if (answered) {
            val nextText = if (playerTurn == 0) "Next (Player 2)" else "Next Question"

            Button(
                onClick = {
                    // dacă s-a terminat întrebarea P2 și era ultima întrebare
                    val willFinish =
                        (playerTurn == 1) && (questionIndex == questions.lastIndex)
                    if (willFinish) onFinish(p1Score, p2Score) else goNext()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(nextText)
            }
        }
    }
}

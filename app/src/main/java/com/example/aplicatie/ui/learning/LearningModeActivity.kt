package com.example.aplicatie.ui.learning

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aplicatie.ui.theme.AplicatieTheme
import kotlinx.coroutines.delay

class LearningModeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val topic = intent.getStringExtra("topic") ?: "cpp" // "cpp", "java", "python"

        // pick questions based on topic
        val questions = when (topic) {
            "java" -> javaQuestions
            "python" -> pythonQuestions
            else -> cppQuestions
        }

        setContent {
            AplicatieTheme {
                Surface {
                    LearningModeScreen(
                        topic = topic,
                        questions = questions
                    )
                }
            }
        }
    }
}

// ---------- Data model ----------
data class LearningQuestion(
    val id: Int,
    val question: String,
    val answer: String
)

// ---------- Sample question sets (extinde cum vrei) ----------

// C++
private val cppQuestions = listOf(
    LearningQuestion(
        1,
        "In C++, which header is required for std::cout?",
        "#include <iostream>"
    ),
    LearningQuestion(
        2,
        "What does the keyword 'virtual' enable in C++?",
        "Runtime polymorphism, allowing overriding in derived classes."
    ),
    LearningQuestion(
        3,
        "What is the average time complexity of std::sort?",
        "O(n log n) in most standard library implementations."
    ),
    LearningQuestion(
        4,
        "What is a reference in C++?",
        "An alias for another variable; it must be initialized and cannot be reseated."
    ),
    LearningQuestion(
        5,
        "What is RAII in C++?",
        "Resource Acquisition Is Initialization – resources are tied to object lifetime."
    )
)

// Java
private val javaQuestions = listOf(
    LearningQuestion(
        1,
        "Which method is the entry point of a Java application?",
        "public static void main(String[] args)"
    ),
    LearningQuestion(
        2,
        "What does 'static' mean on a method?",
        "The method belongs to the class, not to a specific instance."
    ),
    LearningQuestion(
        3,
        "Which collection in Java does not allow duplicate elements?",
        "java.util.Set and its implementations."
    ),
    LearningQuestion(
        4,
        "What is the JVM?",
        "Java Virtual Machine – it executes compiled Java bytecode."
    ),
    LearningQuestion(
        5,
        "What is a checked exception?",
        "An exception that must be declared or handled (subclasses of Exception, excluding RuntimeException)."
    )
)

// Python
private val pythonQuestions = listOf(
    LearningQuestion(
        1,
        "Which symbol starts a comment in Python?",
        "The # character starts a single-line comment."
    ),
    LearningQuestion(
        2,
        "How do you define a function in Python?",
        "Using 'def func_name(params):' followed by an indented block."
    ),
    LearningQuestion(
        3,
        "What is a list in Python?",
        "An ordered, mutable collection defined with [ ]."
    ),
    LearningQuestion(
        4,
        "What does len() return?",
        "The length (number of items) in a sequence or collection."
    ),
    LearningQuestion(
        5,
        "What is a dictionary in Python?",
        "An unordered collection of key-value pairs defined with { }."
    )
)

@Composable
fun LearningModeScreen(
    topic: String,
    questions: List<LearningQuestion>
) {
    // track which questions are marked as learned
    var checkedIds by rememberSaveable { mutableStateOf(setOf<Int>()) }
    // track which questions are expanded (show answer)
    var expandedIds by rememberSaveable { mutableStateOf(setOf<Int>()) }

    val total = questions.size.coerceAtLeast(1)
    val progress = checkedIds.size.toFloat() / total.toFloat()

    var showConfetti by remember { mutableStateOf(false) }

    // trigger confetti when progress reaches 100%
    LaunchedEffect(progress) {
        if (progress >= 1f && questions.isNotEmpty()) {
            showConfetti = true
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF98A5D6)) // light purple background
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Title
            Text(
                text = "Learning mode – ${topic.uppercase()}",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(16.dp))

            // Progress bar and label
            Text(
                text = "Progress: ${checkedIds.size} / $total",
                color = Color.Black,
                fontSize = 16.sp
            )
            Spacer(Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp),
                color = Color(0xFF4CAF50),            // green bar
                trackColor = Color(0xFFCCCCCC)
            )

            Spacer(Modifier.height(16.dp))

            // Questions list
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(questions) { q ->
                    QuestionItem(
                        question = q,
                        isChecked = checkedIds.contains(q.id),
                        isExpanded = expandedIds.contains(q.id),
                        onToggleChecked = { checked ->
                            checkedIds = if (checked) {
                                checkedIds + q.id
                            } else {
                                checkedIds - q.id
                            }
                        },
                        onToggleExpanded = {
                            expandedIds = if (expandedIds.contains(q.id)) {
                                expandedIds - q.id
                            } else {
                                expandedIds + q.id
                            }
                        }
                    )
                    Spacer(Modifier.height(8.dp))
                }
            }
        }

        if (showConfetti) {
            ConfettiOverlay(
                onDismiss = { showConfetti = false }
            )
        }
    }
}

@Composable
private fun QuestionItem(
    question: LearningQuestion,
    isChecked: Boolean,
    isExpanded: Boolean,
    onToggleChecked: (Boolean) -> Unit,
    onToggleExpanded: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggleExpanded() },
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE6E6FA)  // very light purple card
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = isChecked,
                    onCheckedChange = { onToggleChecked(it) }
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = question.question,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    modifier = Modifier.weight(1f)
                )
            }

            if (isExpanded) {
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Answer:",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333),
                    fontSize = 14.sp
                )
                Text(
                    text = question.answer,
                    fontSize = 14.sp,
                    color = Color(0xFF222222)
                )
            }
        }
    }
}

// ---------- Confetti overlay ----------

@Composable
private fun ConfettiOverlay(
    onDismiss: () -> Unit
) {
    // auto-dismiss after 2.5 seconds
    LaunchedEffect(Unit) {
        delay(2500)
        onDismiss()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0x80000000)) // semi-transparent black
            .noRippleClickable { onDismiss() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "🎉🎊✨", fontSize = 40.sp, modifier = Modifier.padding(bottom = 8.dp))
            Text(
                text = "Awesome! You completed 100% of this topic!",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Keep going, future senior dev 😎",
                fontSize = 16.sp,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }
    }
}

// helper: clickable without ripple
@Composable
private fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier =
    this.clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        onClick = onClick
    )

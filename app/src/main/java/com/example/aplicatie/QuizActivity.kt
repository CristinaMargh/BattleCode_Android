package com.example.aplicatie

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.*
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.aplicatie.data.UserRepository

class QuizActivity : AppCompatActivity() {

    // ---------- Questions by difficulty (EN) ----------
    private val easyQuestions = listOf(
        Question("What value does the `main()` function return in C/C++ if everything went successfully?",
            listOf("0", "1", "-1", "void"), 0),
        Question("Which statement completely stops the execution of a `for` loop?",
            listOf("continue", "break", "return", "exit"), 1),
        Question("How is the NULL character represented in ASCII?",
            listOf("'\\0'", "'NULL'", "'\\n'", "'0'"), 0)
    )

    private val mediumQuestions = listOf(
        Question("What is the name of the memory area where local variables are allocated?",
            listOf("Heap", "Stack", "Data segment", "Text segment"), 1),
        Question("Which data structure is used by the call stack of executing functions?",
            listOf("Queue", "Heap", "Stack", "Tree"), 2),
        Question("Which operator in C/C++ is used to access members via a pointer to a struct?",
            listOf(".", "->", "::", "#"), 1)
    )

    private val hardQuestions = listOf(
        Question("What is the typical size of an `int` in C on modern (64-bit) systems?",
            listOf("2 bytes", "4 bytes", "8 bytes", "Depends on the compiler"), 1),
        Question("What is the latest C++ standard (as of 2023)?",
            listOf("C++11", "C++17", "C++20", "C++23"), 3),
        Question("Which terminal command compiles a `main.c` file with GCC?",
            listOf("gcc main.c", "g++ main.c", "make main", "compile main.c"), 0),
        Question("What is the average time complexity for searching in a `hash map` (C++/Java)?",
            listOf("O(n)", "O(1)", "O(log n)", "O(n log n)"), 1)
    )

    // Will be filled based on difficulty
    private var questions: List<Question> = emptyList()

    private val wrongQuestions = mutableListOf<String>()
    private val wrongCorrectAnswers = mutableListOf<String>()

    private var currentQuestionIndex = 0
    private var score = 0
    private lateinit var timer: CountDownTimer
    private var startTime = 0L

    private lateinit var answerButtons: List<Button>
    private lateinit var questionText: TextView
    private lateinit var timerText: TextView
    private lateinit var happyCat: ImageView
    private lateinit var angryCat: ImageView
    private lateinit var nextQuestionButton: Button
    private lateinit var currentUsername: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        // Username (optional)
        currentUsername = intent.getStringExtra("username") ?: "ANONIM"

        // Difficulty -> select questions
        val difficulty = intent.getStringExtra("difficulty") ?: "medium"
        questions = when (difficulty) {
            "easy" -> easyQuestions.shuffled().take(5)
            "hard" -> hardQuestions.shuffled().take(5)
            else -> mediumQuestions.shuffled().take(5)
        }

        // Bind views
        answerButtons = listOf(
            findViewById(R.id.answer1),
            findViewById(R.id.answer2),
            findViewById(R.id.answer3),
            findViewById(R.id.answer4)
        )
        questionText = findViewById(R.id.question_text)
        timerText = findViewById(R.id.timer_text)
        happyCat = findViewById(R.id.happy_cat)
        angryCat = findViewById(R.id.angry_cat)
        nextQuestionButton = findViewById(R.id.next_question_button)

        nextQuestionButton.setOnClickListener {
            currentQuestionIndex++
            showQuestion()
        }

        showQuestion()
    }

    /** Short vibration on wrong answer */
    private fun vibrateError() {
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(
                VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE)
            )
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(200)
        }
    }

    private fun showQuestion() {
        if (currentQuestionIndex >= questions.size) {
            UserRepository().updateHighScore(currentUsername, score)
            val intent = Intent(this, ResultActivity::class.java)
            intent.putExtra("score", score)
            intent.putStringArrayListExtra("wrongQuestions", ArrayList(wrongQuestions))
            intent.putStringArrayListExtra("wrongCorrectAnswers", ArrayList(wrongCorrectAnswers))
            startActivity(intent)
            finish()
            return
        }

        happyCat.visibility = View.GONE
        angryCat.visibility = View.GONE
        nextQuestionButton.visibility = View.GONE

        val question = questions[currentQuestionIndex]
        questionText.text = question.text

        answerButtons.forEachIndexed { index, button ->
            button.setBackgroundResource(R.drawable.r_a_b)
            button.setTextColor(Color.BLACK)
            button.isClickable = true
            button.text = question.options[index]

            button.setOnClickListener {
                timer.cancel()
                val timeTaken = SystemClock.elapsedRealtime() - startTime

                if (index == question.correctAnswerIndex) {
                    score += calculateScore(timeTaken)
                    UserRepository().updateHighScore(currentUsername, score)
                    button.setBackgroundResource(R.drawable.answer_correct)
                    happyCat.visibility = View.VISIBLE
                    happyCat.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_up))
                } else {
                    button.setBackgroundResource(R.drawable.answer_wrong)
                    angryCat.visibility = View.VISIBLE
                    angryCat.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_up))
                    vibrateError()
                    wrongQuestions.add(question.text)
                    wrongCorrectAnswers.add(question.options[question.correctAnswerIndex])
                }

                answerButtons.forEach { it.isClickable = false }
                answerButtons[question.correctAnswerIndex].setBackgroundResource(R.drawable.answer_correct)
                nextQuestionButton.visibility = View.VISIBLE
            }
        }

        startTime = SystemClock.elapsedRealtime()
        startTimer()
    }

    private fun startTimer() {
        timer = object : CountDownTimer(10_000, 1_000) {
            override fun onTick(millisUntilFinished: Long) {
                timerText.text = "Time left: ${millisUntilFinished / 1000}s"
            }

            override fun onFinish() {
                answerButtons.forEachIndexed { i, b ->
                    b.isClickable = false
                    if (i == questions[currentQuestionIndex].correctAnswerIndex) {
                        b.setBackgroundResource(R.drawable.answer_correct)
                    }
                }
                angryCat.visibility = View.VISIBLE
                angryCat.startAnimation(AnimationUtils.loadAnimation(this@QuizActivity, R.anim.slide_up))
                nextQuestionButton.visibility = View.VISIBLE
            }
        }.start()
    }

    private fun calculateScore(timeTaken: Long): Int =
        (10_000 - timeTaken).toInt() / 1000

    data class Question(val text: String, val options: List<String>, val correctAnswerIndex: Int)
}

package com.example.aplicatie

import android.content.Intent
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.widget.*
import kotlin.system.*
import android.graphics.Color
import android.view.View
import android.view.animation.AnimationUtils
import com.example.aplicatie.data.UserRepository

class QuizActivity : AppCompatActivity() {

    private val allQuestions = listOf(
        Question("Care este dimensiunea tipică a unui `int` în C pe sistemele moderne (64-bit)?",
            listOf("2 bytes", "4 bytes", "8 bytes", "Depinde de compilator"), 1),

        Question("Ce valoare returnează funcția `main()` în C/C++ dacă totul a decurs cu succes?",
            listOf("0", "1", "-1", "void"), 0),

        Question("Ce structură de date folosește stiva funcțiilor în execuție?",
            listOf("Queue", "Heap", "Stack", "Tree"), 2),

        Question("Ce operator în C/C++ este folosit pentru a accesa membrii unui pointer către structură?",
            listOf(".", "->", "::", "#"), 1),

        Question("Cum se numește zona de memorie unde sunt alocate variabilele locale?",
            listOf("Heap", "Stack", "Data segment", "Text segment"), 1),

        Question("Care este complexitatea medie a căutării într-un `hash map` (C++/Java)?",
            listOf("O(n)", "O(1)", "O(log n)", "O(n log n)"), 1),

        Question("Ce instrucțiune oprește complet execuția unui ciclu `for`?",
            listOf("continue", "break", "return", "exit"), 1),

        Question("Care este standardul cel mai recent pentru C++ (în 2023)?",
            listOf("C++11", "C++17", "C++20", "C++23"), 3),

        Question("Ce comandă folosești în terminal pentru a compila un fișier `main.c` cu gcc?",
            listOf("gcc main.c", "g++ main.c", "make main", "compile main.c"), 0),

        Question("Cum este reprezentat caracterul NULL în ASCII?",
            listOf("'\\0'", "'NULL'", "'\\n'", "'0'"), 0)
    )

    private val questions = allQuestions.shuffled().take(5)


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
        currentUsername = intent.getStringExtra("username") ?: "ANONIM"
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

    private fun showQuestion() {
        if (currentQuestionIndex >= questions.size) {
            UserRepository().updateHighScore(currentUsername, score)
            val intent = Intent(this, ResultActivity::class.java)
            intent.putExtra("score", score)
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
            button.setTextColor(resources.getColor(android.R.color.black))
            button.isClickable = true
            button.isFocusable = true
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
                }

                answerButtons.forEach {
                    it.isClickable = false
                    it.isFocusable = false
                }

                answerButtons[question.correctAnswerIndex].setBackgroundResource(R.drawable.answer_correct)
                nextQuestionButton.visibility = View.VISIBLE
            }
        }

        startTime = SystemClock.elapsedRealtime()
        startTimer()
    }

    private fun startTimer() {
        timer = object : CountDownTimer(10000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timerText.text = "Timp rămas: ${millisUntilFinished / 1000}s"
            }

            override fun onFinish() {
                answerButtons.forEachIndexed { i, b ->
                    b.isClickable = false
                    b.isFocusable = false
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

    private fun calculateScore(timeTaken: Long): Int {
        return (10000 - timeTaken).toInt() / 1000
    }

    data class Question(val text: String, val options: List<String>, val correctAnswerIndex: Int)
}

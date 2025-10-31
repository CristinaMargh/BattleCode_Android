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
import com.example.aplicatie.util.LocationLanguage   // <-- important

class QuizActivity : AppCompatActivity() {

    // ---------------------- EN questions ----------------------
    private val enEasy = listOf(
        Q("What value does the `main()` function return in C/C++ if everything went successfully?",
            listOf("0", "1", "-1", "void"), 0),
        Q("Which statement completely stops the execution of a `for` loop?",
            listOf("continue", "break", "return", "exit"), 1),
        Q("How is the NULL character represented in ASCII?",
            listOf("'\\0'", "'NULL'", "'\\n'", "'0'"), 0),
    )
    private val enMedium = listOf(
        Q("What is the name of the memory area where local variables are allocated?",
            listOf("Heap", "Stack", "Data segment", "Text segment"), 1),
        Q("Which data structure is used by the call stack of executing functions?",
            listOf("Queue", "Heap", "Stack", "Tree"), 2),
        Q("Which operator in C/C++ is used to access members via a pointer to a struct?",
            listOf(".", "->", "::", "#"), 1),
    )
    private val enHard = listOf(
        Q("What is the typical size of an `int` in C on modern (64-bit) systems?",
            listOf("2 bytes", "4 bytes", "8 bytes", "Depends on the compiler"), 1),
        Q("What is the latest C++ standard (as of 2023)?",
            listOf("C++11", "C++17", "C++20", "C++23"), 3),
        Q("Which terminal command compiles a `main.c` file with GCC?",
            listOf("gcc main.c", "g++ main.c", "make main", "compile main.c"), 0),
        Q("What is the average time complexity for searching in a `hash map` (C++/Java)?",
            listOf("O(n)", "O(1)", "O(log n)", "O(n log n)"), 1),
    )

    // ---------------------- RO questions ----------------------
    private val roEasy = listOf(
        Q("Ce valoare întoarce funcția `main()` în C/C++ dacă programul s-a terminat cu succes?",
            listOf("0", "1", "-1", "void"), 0),
        Q("Ce instrucțiune oprește complet execuția unui ciclu `for`?",
            listOf("continue", "break", "return", "exit"), 1),
        Q("Cum este reprezentat caracterul NULL în ASCII?",
            listOf("'\\0'", "'NULL'", "'\\n'", "'0'"), 0),
    )
    private val roMedium = listOf(
        Q("Cum se numește zona de memorie unde sunt alocate variabilele locale?",
            listOf("Heap", "Stack", "Data segment", "Text segment"), 1),
        Q("Ce structură de date folosește stiva apelurilor de funcții?",
            listOf("Queue", "Heap", "Stack", "Tree"), 2),
        Q("Ce operator în C/C++ este folosit pentru a accesa membrii printr-un pointer la structură?",
            listOf(".", "->", "::", "#"), 1),
    )
    private val roHard = listOf(
        Q("Care este dimensiunea tipică a unui `int` în C pe sisteme moderne (64-bit)?",
            listOf("2 bytes", "4 bytes", "8 bytes", "Depinde de compilator"), 1),
        Q("Care este cel mai recent standard C++ (în 2023)?",
            listOf("C++11", "C++17", "C++20", "C++23"), 3),
        Q("Ce comandă compilează fișierul `main.c` folosind GCC?",
            listOf("gcc main.c", "g++ main.c", "make main", "compile main.c"), 0),
        Q("Care este complexitatea medie a căutării într-un `hash map` (C++/Java)?",
            listOf("O(n)", "O(1)", "O(log n)", "O(n log n)"), 1),
    )

    // ---------------------- DE questions (exemplu: EN duplicat) ----------------------
    // Le poți traduce ulterior; momentan folosim aceleași ca EN ca să demonstrezi funcționalitatea.
    private val deEasy = enEasy
    private val deMedium = enMedium
    private val deHard = enHard

    // ---------------------- runtime state ----------------------
    private lateinit var questions: List<Q>
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
    private var currentLang: String = "en"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        // Username (optional)
        currentUsername = intent.getStringExtra("username") ?: "ANONIM"

        // ----- language selected by LocationLanguage (default "en")
        currentLang = getSharedPreferences(LocationLanguage.PREFS, MODE_PRIVATE)
            .getString(LocationLanguage.PREF_LANG, "en") ?: "en"

        // ----- Difficulty + Language -> select pool
        val difficulty = intent.getStringExtra("difficulty") ?: "medium"
        questions = pickPool(currentLang, difficulty).shuffled().take(5)

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

    /** Select pool based on language + difficulty. */
    private fun pickPool(lang: String, difficulty: String): List<Q> = when (lang) {
        "ro" -> when (difficulty) {
            "easy" -> roEasy
            "hard" -> roHard
            else -> roMedium
        }
        "de" -> when (difficulty) {
            "easy" -> deEasy
            "hard" -> deHard
            else -> deMedium
        }
        else -> when (difficulty) { // "en" fallback
            "easy" -> enEasy
            "hard" -> enHard
            else -> enMedium
        }
    }

    /** Short vibration on wrong answer */
    private fun vibrateError() {
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
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

        val q = questions[currentQuestionIndex]
        questionText.text = q.text

        answerButtons.forEachIndexed { index, button ->
            button.setBackgroundResource(R.drawable.r_a_b)
            button.setTextColor(Color.BLACK)
            button.isClickable = true
            button.text = q.options[index]

            button.setOnClickListener {
                timer.cancel()
                val timeTaken = SystemClock.elapsedRealtime() - startTime

                if (index == q.correctAnswerIndex) {
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
                    wrongQuestions.add(q.text)
                    wrongCorrectAnswers.add(q.options[q.correctAnswerIndex])
                }

                answerButtons.forEach { it.isClickable = false }
                answerButtons[q.correctAnswerIndex].setBackgroundResource(R.drawable.answer_correct)
                nextQuestionButton.visibility = View.VISIBLE
            }
        }

        startTime = SystemClock.elapsedRealtime()
        startTimer()
    }

    private fun startTimer() {
        timer = object : CountDownTimer(10_000, 1_000) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = millisUntilFinished / 1000
                timerText.text = when (currentLang) {
                    "ro" -> "Timp rămas: ${seconds}s"
                    "de" -> "Verbleibende Zeit: ${seconds}s"
                    else -> "Time left: ${seconds}s"
                }
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

    data class Q(val text: String, val options: List<String>, val correctAnswerIndex: Int)
}

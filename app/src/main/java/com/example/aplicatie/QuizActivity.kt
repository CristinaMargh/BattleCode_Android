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
import com.example.aplicatie.util.LocationLanguage

class QuizActivity : AppCompatActivity() {

    // ========== ENGLISH QUESTIONS ==========

    // C++
    // ---------- EN C++ EASY ----------
    private val enCppEasy = listOf(
        Q("In C++, which header is needed for cout?",
            listOf("<iostream>", "<stdio.h>", "<vector>", "<string>"), 0),
        Q("What does '//' start?",
            listOf("A class", "A single-line comment", "A loop", "A namespace"), 1),
        Q("Which symbol ends a C++ statement?",
            listOf(":", ";", ".", ","), 1),
        Q("Which of these is a valid C++ type?",
            listOf("number", "int", "digit", "var"), 1),
        Q("Which keyword is used to declare a constant?",
            listOf("final", "const", "static", "let"), 1),
        Q("Which of these is the correct main signature (simple)?",
            listOf("int main()", "void main()", "main()", "func main()"), 0),
        Q("Which operator is used for assignment?",
            listOf("=", "==", ":=", "->"), 0),
        Q("Which of these is a correct comment style in C++?",
            listOf("# comment", "// comment", "-- comment", "% comment"), 1)
    )

    // ---------- EN C++ MEDIUM ----------
    private val enCppMedium = listOf(
        Q("Which keyword creates an object on the heap?",
            listOf("new", "malloc", "alloc", "create"), 0),
        Q("What does 'virtual' enable in C++?",
            listOf("polymorphism", "multithreading", "templates", "namespaces"), 0),
        Q("Which container provides fast random access?",
            listOf("std::vector", "std::list", "std::queue", "std::stack"), 0),
        Q("Which header do you include for std::vector?",
            listOf("<vector>", "<array>", "<list>", "<deque>"), 0),
        Q("What does RAII stand for?",
            listOf("Resource Acquisition Is Initialization",
                "Runtime Allocation Is Immediate",
                "Random Access In Iterators",
                "Reference And Index Interface"), 0),
        Q("What does '&' mean in a function parameter (e.g. int& x)?",
            listOf("pointer", "reference", "array", "rvalue"), 1),
        Q("Which of these is NOT a standard container?",
            listOf("std::map", "std::set", "std::hashmap", "std::deque"), 2),
        Q("Which C++ cast is safest / most explicit?",
            listOf("static_cast", "reinterpret_cast", "C-style cast", "const_cast"), 0)
    )

    // ---------- EN C++ HARD ----------
    private val enCppHard = listOf(
        Q("What is the time complexity of std::sort on average?",
            listOf("O(n)", "O(n log n)", "O(log n)", "O(n^2)"), 1),
        Q("Which container keeps keys ordered?",
            listOf("std::map", "std::unordered_map", "std::vector", "std::queue"), 0),
        Q("What is the underlying structure typically used by std::map?",
            listOf("Red-Black Tree", "Hash table", "Array", "Skip list"), 0),
        Q("Which C++11 feature allows functions to return different types depending on template params?",
            listOf("auto return type", "decltype", "lambda", "override"), 1),
        Q("What does 'move semantics' mainly optimize?",
            listOf("Copying large objects", "Virtual functions", "Multiple inheritance", "Preprocessor directives"), 0),
        Q("Which smart pointer expresses shared ownership?",
            listOf("std::unique_ptr", "std::shared_ptr", "std::auto_ptr", "std::raw_ptr"), 1),
        Q("Which keyword prevents a class from being inherited?",
            listOf("final", "sealed", "static", "private"), 0),
        Q("What does the 'noexcept' specifier tell the compiler?",
            listOf("Function will not throw", "Function is inline", "Function is virtual", "Function is pure"), 0)
    )


    // Java
    // ---------- EN JAVA EASY ----------
    private val enJavaEasy = listOf(
        Q(
            "Which keyword defines a class in Java?",
            listOf("class", "struct", "object", "def"),
            0
        ),
        Q(
            "Which method is the entry point in Java?",
            listOf("start()", "run()", "main()", "init()"),
            2
        ),
        Q(
            "Which of these is a valid Java identifier?",
            listOf("1name", "name_1", "name-1", "name!"),
            1
        ),
        Q(
            "Which keyword is used to create a new object?",
            listOf("new", "create", "make", "alloc"),
            0
        ),
        Q(
            "Which type is used for decimal numbers?",
            listOf("int", "double", "char", "boolean"),
            1
        ),
        Q(
            "Which package is imported automatically?",
            listOf("java.lang", "java.util", "java.io", "none"),
            0
        ),
        Q(
            "How do you write a single-line comment in Java?",
            listOf("// comment", "# comment", "-- comment", "/* comment */"),
            0
        )
    )

    // ---------- EN JAVA MEDIUM ----------
    private val enJavaMedium = listOf(
        Q(
            "Which collection doesn't allow duplicates?",
            listOf("List", "Set", "ArrayList", "Queue"),
            1
        ),
        Q(
            "What does 'static' mean on a method?",
            listOf("belongs to the class", "is final", "is abstract", "runs faster"),
            0
        ),
        Q(
            "Which keyword is used to handle exceptions?",
            listOf("try", "error", "catchError", "throwCatch"),
            0
        ),
        Q(
            "Which class is the root of all classes in Java?",
            listOf("Object", "Base", "Root", "Core"),
            0
        ),
        Q(
            "Which interface is implemented by ArrayList?",
            listOf("List", "Map", "Set", "Queue"),
            0
        ),
        Q(
            "What is the default value of a boolean field?",
            listOf("true", "false", "null", "0"),
            1
        ),
        Q(
            "Which access modifier makes a member visible only in the same package?",
            listOf("public", "private", "protected", "no modifier (package-private)"),
            3
        ),
        Q(
            "What does 'final' on a variable mean?",
            listOf("cannot be changed", "is static", "is public", "is synchronized"),
            0
        )
    )

    // ---------- EN JAVA HARD ----------
    private val enJavaHard = listOf(
        Q(
            "Which interface is used for lambda expressions in Java 8?",
            listOf("Functional interface", "Runnable", "Serializable", "AutoCloseable"),
            0
        ),
        Q(
            "Which keyword is used to inherit a class?",
            listOf("inherits", "extends", "implements", "super"),
            1
        ),
        Q(
            "Which keyword is used to implement an interface?",
            listOf("extends", "implements", "interface", "override"),
            1
        ),
        Q(
            "Which collection is thread-safe by default?",
            listOf("Vector", "ArrayList", "LinkedList", "HashSet"),
            0
        ),
        Q(
            "What does the 'volatile' keyword guarantee?",
            listOf("visibility between threads", "more speed", "immutability", "serialization"),
            0
        ),
        Q(
            "Which part of the JVM actually executes the bytecode?",
            listOf("JIT / Interpreter", "GC", "ClassLoader", "Javadoc"),
            0
        ),
        Q(
            "Which exception must be either caught or declared?",
            listOf("IOException", "RuntimeException", "NullPointerException", "ArithmeticException"),
            0
        ),
        Q(
            "Which statement is true about garbage collection?",
            listOf("It frees unreachable objects", "It must be called manually", "It deletes files", "It runs only once"),
            0
        )
    )


    // Python
    private val enPythonEasy = listOf(
        Q("Which symbol starts a comment in Python?", listOf("#", "//", "/* */", "--"), 0),
        Q("How do you print in Python 3?", listOf("echo()", "print()", "printf()", "cout <<"), 1),
    )
    private val enPythonMedium = listOf(
        Q("What is a correct list literal?", listOf("{1,2,3}", "(1,2,3)", "[1,2,3]", "<1,2,3>"), 2),
        Q("How do you define a function?", listOf("func my()", "def my()", "function my()", "fn my()"), 1),
    )
    private val enPythonHard = listOf(
        Q("Which keyword is used for exception handling?", listOf("catch", "handle", "try", "except"), 3),
        Q("What does 'len()' return?", listOf("type", "length", "address", "hash"), 1),
    )


    // ========== ROMANIAN QUESTIONS ==========

    // C++
    private val roCppEasy = listOf(
        Q("În C++, ce header e necesar pentru cout?", listOf("<iostream>", "<stdio.h>", "<vector>", "<string>"), 0),
        Q("Ce începe cu '//'?", listOf("O clasă", "Un comentariu pe o linie", "Un ciclu", "Un namespace"), 1),
    )
    private val roCppMedium = listOf(
        Q("Ce cuvânt cheie alocă pe heap?", listOf("new", "malloc", "alloc", "create"), 0),
        Q("Ce permite cuvântul 'virtual' în C++?", listOf("polimorfism", "multithreading", "template-uri", "namespace-uri"), 0),
    )
    private val roCppHard = listOf(
        Q("Care e complexitatea medie a lui std::sort?", listOf("O(n)", "O(n log n)", "O(log n)", "O(n^2)"), 1),
        Q("Ce container păstrează cheile ordonate?", listOf("std::map", "std::unordered_map", "std::vector", "std::queue"), 0),
    )

    // Java
    private val roJavaEasy = listOf(
        Q("Ce cuvânt definește o clasă în Java?", listOf("class", "struct", "object", "def"), 0),
        Q("Ce metodă e punctul de intrare în Java?", listOf("start()", "run()", "main()", "init()"), 2),
    )
    private val roJavaMedium = listOf(
        Q("Ce colecție nu permite duplicate?", listOf("List", "Set", "ArrayList", "Queue"), 1),
        Q("Ce înseamnă 'static' pe o metodă?", listOf("aparține clasei", "este final", "este abstract", "rulează mai repede"), 0),
    )
    private val roJavaHard = listOf(
        Q("Ce interfață se folosește la lambda în Java 8?", listOf("Functional interface", "Runnable", "Serializable", "AutoCloseable"), 0),
        Q("Ce cuvânt se folosește pentru moștenire?", listOf("inherits", "extends", "implements", "super"), 1),
    )

    // Python
    private val roPythonEasy = listOf(
        Q("Ce simbol începe un comentariu în Python?", listOf("#", "//", "/* */", "--"), 0),
        Q("Cum afișezi în Python 3?", listOf("echo()", "print()", "printf()", "cout <<"), 1),
    )
    private val roPythonMedium = listOf(
        Q("Care e un literal de listă corect?", listOf("{1,2,3}", "(1,2,3)", "[1,2,3]", "<1,2,3>"), 2),
        Q("Cum definești o funcție?", listOf("func f()", "def f()", "function f()", "fn f()"), 1),
    )
    private val roPythonHard = listOf(
        Q("Ce cuvânt se folosește la tratarea excepțiilor?", listOf("catch", "handle", "try", "except"), 3),
        Q("Ce întoarce 'len()'?", listOf("tipul", "lungimea", "adresa", "hash-ul"), 1),
    )

    // German -> deocamdată copiem engleza
    private val deCppEasy = enCppEasy
    private val deCppMedium = enCppMedium
    private val deCppHard = enCppHard
    private val deJavaEasy = enJavaEasy
    private val deJavaMedium = enJavaMedium
    private val deJavaHard = enJavaHard
    private val dePythonEasy = enPythonEasy
    private val dePythonMedium = enPythonMedium
    private val dePythonHard = enPythonHard

    // runtime
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

        currentUsername = intent.getStringExtra("username") ?: "ANONIM"

        // language saved by LocationLanguage
        currentLang = getSharedPreferences(LocationLanguage.PREFS, MODE_PRIVATE)
            .getString(LocationLanguage.PREF_LANG, "en") ?: "en"

        val topic = intent.getStringExtra("topic") ?: "cpp"       // cpp / java / python
        val difficulty = intent.getStringExtra("difficulty") ?: "medium"

        questions = pickQuestions(currentLang, topic, difficulty).shuffled().take(5)

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

    private fun pickQuestions(lang: String, topic: String, difficulty: String): List<Q> {
        return when (lang) {
            "ro" -> pickRo(topic, difficulty)
            "de" -> pickDe(topic, difficulty)
            else -> pickEn(topic, difficulty)
        }
    }

    private fun pickEn(topic: String, difficulty: String): List<Q> = when (topic) {
        "java" -> when (difficulty) {
            "easy" -> enJavaEasy
            "hard" -> enJavaHard
            else -> enJavaMedium
        }
        "python" -> when (difficulty) {
            "easy" -> enPythonEasy
            "hard" -> enPythonHard
            else -> enPythonMedium
        }
        else -> when (difficulty) { // cpp
            "easy" -> enCppEasy
            "hard" -> enCppHard
            else -> enCppMedium
        }
    }

    private fun pickRo(topic: String, difficulty: String): List<Q> = when (topic) {
        "java" -> when (difficulty) {
            "easy" -> roJavaEasy
            "hard" -> roJavaHard
            else -> roJavaMedium
        }
        "python" -> when (difficulty) {
            "easy" -> roPythonEasy
            "hard" -> roPythonHard
            else -> roPythonMedium
        }
        else -> when (difficulty) { // cpp
            "easy" -> roCppEasy
            "hard" -> roCppHard
            else -> roCppMedium
        }
    }

    private fun pickDe(topic: String, difficulty: String): List<Q> = when (topic) {
        "java" -> when (difficulty) {
            "easy" -> deJavaEasy
            "hard" -> deJavaHard
            else -> deJavaMedium
        }
        "python" -> when (difficulty) {
            "easy" -> dePythonEasy
            "hard" -> dePythonHard
            else -> dePythonMedium
        }
        else -> when (difficulty) { // cpp
            "easy" -> deCppEasy
            "hard" -> deCppHard
            else -> deCppMedium
        }
    }

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
                val s = millisUntilFinished / 1000
                timerText.text = when (currentLang) {
                    "ro" -> "Timp rămas: ${s}s"
                    "de" -> "Verbleibende Zeit: ${s}s"
                    else -> "Time left: ${s}s"
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

package com.example.aplicatie

import android.content.Intent
import android.graphics.Color
import android.os.*
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.aplicatie.util.ReviewQuestion

class ReviewQuizActivity : AppCompatActivity() {

    private lateinit var questions: List<ReviewQuestion>
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

    private val wrongQuestions = mutableListOf<String>()
    private val wrongCorrectAnswers = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        val reviewQs =
            (intent.getSerializableExtra("reviewQuestions") as? ArrayList<ReviewQuestion>)
                ?: arrayListOf()

        questions = reviewQs.shuffled()

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
            val fade = AnimationUtils.loadAnimation(this, R.anim.fade_out)
            questionText.startAnimation(fade)
            answerButtons.forEach { it.startAnimation(fade) }
            currentQuestionIndex++
            showQuestion()
        }

        showQuestion()
    }

    private fun showQuestion() {
        if (currentQuestionIndex >= questions.size) {
            val intent = Intent(this, ResultActivity::class.java)
            intent.putExtra("score", score)
            intent.putStringArrayListExtra("wrongQuestions", ArrayList(wrongQuestions))
            intent.putStringArrayListExtra("wrongCorrectAnswers", ArrayList(wrongCorrectAnswers))
            // reviewQuestions nu mai trimitem iar, ca e deja review
            startActivity(intent)
            finish()
            return
        }

        happyCat.visibility = View.GONE
        angryCat.visibility = View.GONE
        nextQuestionButton.visibility = View.GONE

        val question = questions[currentQuestionIndex]

        val slideIn = AnimationUtils.loadAnimation(this, R.anim.slide_in_left)
        questionText.text = question.text
        questionText.startAnimation(slideIn)

        answerButtons.forEachIndexed { index, button ->
            button.setBackgroundResource(R.drawable.r_a_b)
            button.setTextColor(Color.BLACK)
            button.isClickable = true
            button.text = question.options[index]
            button.startAnimation(slideIn)

            button.setOnClickListener {
                timer.cancel()
                val timeTaken = SystemClock.elapsedRealtime() - startTime

                if (index == question.correctAnswerIndex) {
                    score += ((10_000 - timeTaken).toInt() / 1000)
                    button.setBackgroundResource(R.drawable.answer_correct)
                    happyCat.visibility = View.VISIBLE
                    happyCat.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_up))
                } else {
                    button.setBackgroundResource(R.drawable.answer_wrong)
                    angryCat.visibility = View.VISIBLE
                    angryCat.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_up))

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
                timerText.text = "Review time left: ${millisUntilFinished / 1000}s"
            }

            override fun onFinish() {
                answerButtons.forEachIndexed { i, b ->
                    b.isClickable = false
                    if (i == questions[currentQuestionIndex].correctAnswerIndex) {
                        b.setBackgroundResource(R.drawable.answer_correct)
                    }
                }
                angryCat.visibility = View.VISIBLE
                angryCat.startAnimation(AnimationUtils.loadAnimation(this@ReviewQuizActivity, R.anim.slide_up))
                nextQuestionButton.visibility = View.VISIBLE
            }
        }.start()
    }
}

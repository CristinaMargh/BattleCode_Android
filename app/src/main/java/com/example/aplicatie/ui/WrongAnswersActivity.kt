package com.example.aplicatie.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class WrongAnswersActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val wrongQuestions =
            intent.getStringArrayListExtra("wrongQuestions") ?: arrayListOf()
        val wrongCorrectAnswers =
            intent.getStringArrayListExtra("wrongCorrectAnswers") ?: arrayListOf()

        setContent {
            MaterialTheme {
                WrongAnswersScreen(
                    wrongQuestions = wrongQuestions,
                    correctAnswers = wrongCorrectAnswers
                )
            }
        }
    }
}

/* ------------------------ Networking helpers ------------------------ */

private fun httpGet(url: String, timeoutMs: Int = 6000): String {
    val conn = (URL(url).openConnection() as HttpURLConnection).apply {
        connectTimeout = timeoutMs
        readTimeout = timeoutMs
        setRequestProperty("User-Agent", "BattleCode/1.0")
    }
    return conn.inputStream.bufferedReader().use { it.readText() }
}

/**
 * API quotes
 *  Dispatchers.IO – non-main thread)
 */
private suspend fun fetchProgrammingQuote(): Pair<String, String> = withContext(Dispatchers.IO) {
    val sources = listOf(
        "https://programming-quotesapi.vercel.app/api/random" to "vercel1",

        "https://programming-quotes-api.vercel.app/api/random" to "vercel2",
        "https://api.quotable.io/random?tags=technology|famous-quotes" to "quotable",

        "https://zenquotes.io/api/random" to "zen"
    )

    for ((url, kind) in sources) {
        try {
            val body = httpGet(url)
            when (kind) {
                "vercel1", "vercel2" -> {
                    val obj = JSONObject(body)
                    val text = obj.optString("en").ifBlank { obj.optString("quote") }
                    val author = obj.optString("author", "Unknown")
                    if (text.isNotBlank()) return@withContext text to author
                }
                "quotable" -> {
                    val obj = JSONObject(body)
                    val text = obj.optString("content")
                    val author = obj.optString("author", "Unknown")
                    if (text.isNotBlank()) return@withContext text to author
                }
                "zen" -> {
                    val arr = JSONArray(body)
                    if (arr.length() > 0) {
                        val obj = arr.getJSONObject(0)
                        val text = obj.optString("q")
                        val author = obj.optString("a", "Unknown")
                        if (text.isNotBlank()) return@withContext text to author
                    }
                }
            }
        } catch (_: Exception) {
        }
    }
    // fallback final
    "Keep coding. Keep learning." to "Unknown"
}

/* ------------------------------ UI ------------------------------ */

@Composable
fun WrongAnswersScreen(
    wrongQuestions: List<String>,
    correctAnswers: List<String>
) {
    var quote by remember { mutableStateOf("Loading quote...") }

    // fetch  background (proces non-main)
    LaunchedEffect(Unit) {
        val (text, author) = fetchProgrammingQuote()
        quote = "\"$text\"\n— $author"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF98A5D6))
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Wrong Questions",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(16.dp))
        val count = minOf(wrongQuestions.size, correctAnswers.size)
        for (i in 0 until count) {
            Text(
                text = "${i + 1}. ${wrongQuestions[i]}",
                fontSize = 16.sp,
                color = Color.Black,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = "Răspuns: ${correctAnswers[i]}",
                fontSize = 14.sp,
                color = Color(0xFF1B1B1B),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            )
        }

        if (count == 0) {
            Text("Nicio întrebare greșită!", color = Color.Black, fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))
        Divider(thickness = 1.dp, color = Color.DarkGray)
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = quote,
            fontSize = 15.sp,
            color = Color.DarkGray,
            modifier = Modifier.padding(8.dp),
            lineHeight = 20.sp
        )
    }
}

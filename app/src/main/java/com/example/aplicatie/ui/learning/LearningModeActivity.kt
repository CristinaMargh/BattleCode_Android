package com.example.aplicatie.ui.learning

import android.content.Context
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
import androidx.compose.ui.platform.LocalContext
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
    // Întrebări inițiale (1-5)
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
    ),

    // Întrebări adăugate (6-25)
    LearningQuestion(
        6,
        "What is the difference between an array and a vector in C++?",
        "An array is a fixed-size contiguous block of memory. std::vector is a dynamic array that can resize itself and manages memory automatically."
    ),
    LearningQuestion(
        7,
        "What is a 'smart pointer' and why use it?",
        "An object that behaves like a pointer but manages memory automatically, preventing memory leaks (e.g., std::unique_ptr, std::shared_ptr)."
    ),
    LearningQuestion(
        8,
        "What are the four pillars of OOP in C++?",
        "Encapsulation, Inheritance, Polymorphism, and Abstraction."
    ),
    LearningQuestion(
        9,
        "Explain the 'this' pointer.",
        "A constant pointer that holds the memory address of the current object, used to refer to instance members of the class."
    ),
    LearningQuestion(
        10,
        "What is the difference between 'new' and 'malloc'?",
        "'new' is an operator that allocates memory and calls the object's constructor; it's type-safe. 'malloc' is a C library function that only allocates raw memory."
    ),
    LearningQuestion(
        11,
        "What is an 'inline' function?",
        "A function where the compiler attempts to replace the function call with the actual function body at compile time, potentially improving performance but possibly increasing code size."
    ),
    LearningQuestion(
        12,
        "What is a 'pure virtual function'?",
        "A virtual function declared with '= 0' (e.g., 'virtual void func() = 0;'). It makes the containing class an abstract class that cannot be instantiated."
    ),
    LearningQuestion(
        13,
        "What is 'template' programming in C++?",
        "A mechanism for generic programming, allowing functions and classes to operate with arbitrary types without being rewritten for each type."
    ),
    LearningQuestion(
        14,
        "What are 'move semantics'?",
        "A C++11 feature that allows resources (like large memory blocks) to be 'moved' from one object to another without deep copying, using rvalue references."
    ),
    LearningQuestion(
        15,
        "What is the 'const' keyword used for?",
        "To declare that a value, parameter, or method will not be modified. It enforces read-only access."
    ),
    LearningQuestion(
        16,
        "How is 'method overloading' different from 'method overriding'?",
        "Overloading is defining multiple functions with the same name but different parameters. Overriding is redefining a virtual function in a derived class (runtime polymorphism)."
    ),
    LearningQuestion(
        17,
        "What is a 'constructor' in C++?",
        "A special member function automatically called when an object is created, used to initialize the object's members."
    ),
    LearningQuestion(
        18,
        "What is an 'initializer list' (member initializer list)?",
        "A syntax used in a constructor to initialize data members before the constructor body executes, mandatory for initializing const members and references."
    ),
    LearningQuestion(
        19,
        "Explain 'shallow copy' vs. 'deep copy'.",
        "Shallow copy copies only the member variables' values (including pointer values). Deep copy creates new copies of all dynamically allocated data pointed to by the members."
    ),
    LearningQuestion(
        20,
        "What is the role of the 'friend' keyword?",
        "It grants non-member functions or other classes access to the 'private' and 'protected' members of the class where it is declared."
    ),
    LearningQuestion(
        21,
        "What is the STL?",
        "The Standard Template Library – a set of C++ template classes that provide common data structures (containers), algorithms, and iterators."
    ),
    LearningQuestion(
        22,
        "What is a 'lambda expression' in C++?",
        "An anonymous function object that can be defined inline, often used to pass simple functions as arguments to algorithms."
    ),
    LearningQuestion(
        23,
        "What is a 'destructor'?",
        "A special member function automatically called when an object goes out of scope or is explicitly deleted, used for cleanup (e.g., releasing dynamically allocated memory)."
    ),
    LearningQuestion(
        24,
        "What is the 'scope resolution operator' (::)?",
        "Used to define a member function outside the class definition, or to access static members or global variables hidden by local variables."
    ),
    LearningQuestion(
        25,
        "What is 'type deduction' (e.g., with 'auto')?",
        "A C++ feature where the compiler automatically determines the data type of a variable based on the type of its initialization expression."
    )
)

// Java
private val javaQuestions = listOf(
    // Întrebări inițiale (1-5)
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
    ),

    // Întrebări adăugate (6-25)
    LearningQuestion(
        6,
        "What is the difference between '==' and '.equals()' in Java?",
        "'==' compares object references (memory addresses), while '.equals()' compares the actual content or value of the objects (if overridden)."
    ),
    LearningQuestion(
        7,
        "What are the four pillars of OOP in Java?",
        "Encapsulation, Inheritance, Polymorphism, and Abstraction."
    ),
    LearningQuestion(
        8,
        "What is an 'abstract class'?",
        "A class that cannot be instantiated and may contain abstract (unimplemented) methods. It is used to define common behavior for subclasses."
    ),
    LearningQuestion(
        9,
        "What is an 'interface' in Java?",
        "A contract that defines a set of abstract methods and constants. A class can implement multiple interfaces, achieving multiple inheritance of types."
    ),
    LearningQuestion(
        10,
        "What is 'method overloading'?",
        "Defining multiple methods in the same class with the same name but different method signatures (different number or types of parameters)."
    ),
    LearningQuestion(
        11,
        "What is 'method overriding'?",
        "Redefining a method in a subclass that is already present in its superclass, inheriting the same signature."
    ),
    LearningQuestion(
        12,
        "What is the purpose of the 'final' keyword?",
        "Applied to a variable, it makes it a constant; to a method, it prevents overriding; to a class, it prevents inheritance."
    ),
    LearningQuestion(
        13,
        "What is the 'Garbage Collector'?",
        "A background process in the JVM that automatically manages memory by reclaiming memory occupied by objects that are no longer referenced by the application."
    ),
    LearningQuestion(
        14,
        "Explain 'public', 'private', and 'protected' access modifiers.",
        "Public: accessible everywhere. Private: accessible only within the defining class. Protected: accessible within the package and by subclasses."
    ),
    LearningQuestion(
        15,
        "What is a 'constructor'?",
        "A special method called automatically when an object is created, used to initialize the object's state (instance variables)."
    ),
    LearningQuestion(
        16,
        "What is the use of the 'super' keyword?",
        "Used to refer to the immediate parent class instance, usually to call the parent's constructor or a method that has been overridden."
    ),
    LearningQuestion(
        17,
        "What is a 'try-with-resources' statement?",
        "A Java 7 feature used to ensure that a resource (an object implementing AutoCloseable) is automatically closed after the try block finishes, regardless of whether an exception occurs."
    ),
    LearningQuestion(
        18,
        "What is the 'String Pool'?",
        "A storage area in the heap memory where String literals are stored. It helps save memory by reusing identical String objects."
    ),
    LearningQuestion(
        19,
        "What is the difference between 'throw' and 'throws'?",
        "'throw' is used inside a method to explicitly throw an exception. 'throws' is used in the method signature to declare which exceptions the method might throw."
    ),
    LearningQuestion(
        20,
        "What is the difference between 'ArrayList' and 'LinkedList'?",
        "ArrayList uses a dynamic array (better for random access). LinkedList uses a doubly-linked list (better for insertion and deletion)."
    ),
    LearningQuestion(
        21,
        "What is 'autoboxing' and 'unboxing'?",
        "Autoboxing is the automatic conversion of a primitive type (int, char) into its corresponding wrapper class object (Integer, Character). Unboxing is the reverse."
    ),
    LearningQuestion(
        22,
        "What is a 'transient' keyword?",
        "A keyword used to mark an instance variable so that it is skipped when the object is serialized (saved to a file or sent over a network)."
    ),
    LearningQuestion(
        23,
        "What is the 'volatile' keyword used for?",
        "Used to indicate that a variable's value may be modified by multiple threads, forcing threads to read its current value from main memory rather than from their local CPU cache."
    ),
    LearningQuestion(
        24,
        "What is the difference between JRE, JDK, and JVM?",
        "JVM (Virtual Machine) runs the code. JRE (Runtime Environment) is the JVM plus libraries needed to run Java programs. JDK (Development Kit) includes the JRE plus development tools (compiler, debugger, etc.)."
    ),
    LearningQuestion(
        25,
        "What is 'Runtime Polymorphism'?",
        "Also known as dynamic method dispatch, it is when the method call is resolved at runtime based on the actual type of the object pointed to by the reference variable (achieved through method overriding)."
    )
)

// Python
private val pythonQuestions = listOf(
    // Întrebări inițiale (1-5)
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
    ),

    LearningQuestion(
        6,
        "What is a tuple in Python?",
        "An ordered, immutable collection defined with ( )."
    ),
    LearningQuestion(
        7,
        "What are the key features of Python?",
        "Key features include dynamic typing, object-oriented, interpreted, extensive standard library, and high-level structure."
    ),
    LearningQuestion(
        8,
        "What are the differences between 'list' and 'tuple'?",
        "Lists are mutable (can be changed), while tuples are immutable (cannot be changed)."
    ),
    LearningQuestion(
        9,
        "Explain Python's 'pass' statement.",
        "It is a null operation; nothing happens when it executes. It's used as a placeholder where a statement is syntactically required but you don't want to execute any code."
    ),
    LearningQuestion(
        10,
        "What is PEP 8?",
        "PEP 8 is Python's style guide for writing clean, readable, and consistent Python code."
    ),
    LearningQuestion(
        11,
        "What is the use of the 'with' statement?",
        "The 'with' statement is used for resource management, ensuring that cleanup code (like closing a file) is executed automatically, even if errors occur (using context managers)."
    ),
    LearningQuestion(
        12,
        "What is the difference between 'range' and 'xrange'?",
        "'range' returns a list (in Python 2) or an iterator (in Python 3), while 'xrange' returns an xrange object (an iterator) which is more memory efficient for large ranges."
    ),
    LearningQuestion(
        13,
        "How is memory managed in Python?",
        "Memory is managed through a private heap space. Python's garbage collector automatically recycles memory that is no longer in use."
    ),
    LearningQuestion(
        14,
        "What is the purpose of 'self' in class methods?",
        "'self' is a reference to the instance of the class (the object) and is the first argument passed to any instance method."
    ),
    LearningQuestion(
        15,
        "What is method overriding?",
        "It is a feature where a child class provides a specific implementation for a method that is already defined in its parent class."
    ),
    LearningQuestion(
        16,
        "What are Python Decorators?",
        "Decorators are functions that take another function as an argument and extend or modify its behavior without explicitly changing its source code."
    ),
    LearningQuestion(
        17,
        "What is a generator in Python?",
        "A generator is a function that returns an iterator object using the 'yield' keyword instead of 'return'. It generates values lazily (one at a time), saving memory."
    ),
    LearningQuestion(
        18,
        "What is the Global Interpreter Lock (GIL)?",
        "The GIL is a mutex (lock) that prevents multiple native threads from executing Python bytecodes at once, ensuring that only one thread runs in the interpreter at any given time."
    ),
    LearningQuestion(
        19,
        "How do you handle exceptions in Python?",
        "Using the 'try', 'except', 'else', and 'finally' blocks."
    ),
    LearningQuestion(
        20,
        "What is slicing in Python?",
        "A method of extracting a subsequence from a sequence (like a list or string) using the notation sequence[start:stop:step]."
    ),
    LearningQuestion(
        21,
        "What is the difference between shallow copy and deep copy?",
        "A shallow copy creates a new object but inserts references to the nested objects found in the original. A deep copy creates a new object and recursively inserts copies of the nested objects."
    ),
    LearningQuestion(
        22,
        "Explain 'break', 'continue', and 'pass' statements.",
        "'break' terminates the loop, 'continue' skips the rest of the current iteration, and 'pass' does nothing (placeholder)."
    ),
    LearningQuestion(
        23,
        "How do you access environment variables in Python?",
        "By using the 'os' module, specifically 'os.environ' or 'os.getenv()'."
    ),
    LearningQuestion(
        24,
        "What is the difference between 'is' and '==' operators?",
        "'==' checks if two objects have the same value (equality), while 'is' checks if two variables refer to the *exact same object in memory* (identity)."
    ),
    LearningQuestion(
        25,
        "What are modules and packages in Python?",
        "A module is a single file (.py) containing code. A package is a directory containing multiple modules and an __init__.py file, used to structure a large application."
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

    val context = LocalContext.current

    LaunchedEffect(progress) {
        if (progress >= 1f) {
            val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
            val username = prefs.getString("username", "ANONIM") ?: "ANONIM"

            // topic e "cpp" / "java" / "python"
            val key = "award_${username}_$topic"
            prefs.edit().putBoolean(key, true).apply()
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

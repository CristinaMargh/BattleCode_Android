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
        Q("Which symbol starts a comment in Python?",
            listOf("#", "//", "/* */", "--"), 0),
        Q("How do you print in Python 3?",
            listOf("echo()", "print()", "printf()", "cout <<"), 1),
        Q("How do you start a function definition?",
            listOf("function myFunc():", "def myFunc():", "func myFunc()", "define myFunc()"), 1),
        Q("Which of these is NOT a valid variable name?",
            listOf("my_var", "2value", "_count", "name1"), 1),
        Q("How do you start a block of code (like an if statement)?",
            listOf("With { }", "With begin/end", "With indentation", "With parentheses"), 2),
        Q("What is the correct file extension for Python files?",
            listOf(".py", ".pt", ".p", ".python"), 0),
        Q("What does `print(3 + 4 * 2)` output?",
            listOf("14", "11", "10", "8"), 1),
        Q("What type is the result of `input()` by default?",
            listOf("int", "str", "float", "bool"), 1)
    )
    private val enPythonMedium = listOf(
        Q("What is a correct list literal?",
            listOf("{1,2,3}", "(1,2,3)", "[1,2,3]", "<1,2,3>"), 2),
        Q("How do you define a function?",
            listOf("func my()", "def my()", "function my()", "fn my()"), 1),
        Q("Which data type is immutable?",
            listOf("list", "dict", "tuple", "set"), 2),
        Q("What does `len({'a': 1, 'b': 2})` return?",
            listOf("2", "1", "0", "Error"), 0),
        Q("How do you import a module named math?",
            listOf("include math", "require math", "import math", "load math"), 2),
        Q("Which keyword is used to create a class?",
            listOf("object", "class", "define", "type"), 1),
        Q("Which function converts a string to an integer?",
            listOf("toInt()", "int()", "parseInt()", "number()"), 1),
        Q("Which operator is used for exponentiation?",
            listOf("^", "**", "^^", "exp()"), 1)
    )
    private val enPythonHard = listOf(
        Q("Which keyword is used for exception handling?",
            listOf("catch", "handle", "try", "except"), 3),
        Q("What does 'len()' return?",
            listOf("type", "length", "address", "hash"), 1),
        Q("Which statement defines a generator function?",
            listOf("return", "yield", "gen", "async"), 1),
        Q("What is the output of `sorted({3,1,2})`?",
            listOf("[1, 2, 3]", "{1,2,3}", "(1,2,3)", "Error"), 0),
        Q("What will `list(range(0,6,2))` produce?",
            listOf("[0,2,4,6]", "[0,2,4]", "(0,2,4)", "[2,4,6]"), 1),
        Q("Which of these opens a file for writing?",
            listOf("open('f.txt', 'r')", "open('f.txt', 'w')", "open('f.txt', 'rw')", "open('f.txt', 'a+')"), 1),
        Q("What is the output of `bool([])`?",
            listOf("True", "False", "None", "Error"), 1),
        Q("Which decorator is used for defining static methods?",
            listOf("@classmethod", "@staticmethod", "@property", "@abstractmethod"), 1),
        Q("What does `__init__` represent?",
            listOf("Destructor", "Constructor", "Module name", "Import path"), 1),
        Q("Which keyword is used to define an anonymous function?",
            listOf("anon", "lambda", "func", "def"), 1)
    )


    // ========== ROMANIAN QUESTIONS ==========

    // C++
    private val roCppEasy = listOf(
        Q("În C++, ce header e necesar pentru cout?", listOf("<iostream>", "<stdio.h>", "<vector>", "<string>"), 0),
        Q("Ce începe cu '//'?", listOf("O clasă", "Un comentariu pe o linie", "Un ciclu", "Un namespace"), 1),
        Q("Cum declari o variabilă întreagă numită 'var'?", listOf("int var;", "variable int var;", "var int;", "integer var;"), 0),
        Q("Ce operator este folosit pentru MODULUS (restul împărțirii)?", listOf("*", "/", "+", "%"), 3),
        Q("Ce header e necesar pentru a folosi std::string?", listOf("<iostream>", "<string>", "<vector>", "<char>"), 1),
        Q("Cum se citește o valoare de la tastatură în variabila 'x'?", listOf("read(x);", "cin >> x;", "cout << x;", "input(x);"), 1),
        Q("Care este operatorul de comparație pentru 'egal cu'?", listOf("=", "==", "!=", "=>"), 1),
        Q("Ce cuvânt cheie se folosește pentru a returna o valoare dintr-o funcție?", listOf("return", "break", "continue", "exit"), 0)
    )

    private val roCppMedium = listOf(
        Q("Ce cuvânt cheie alocă pe heap?", listOf("new", "malloc", "alloc", "create"), 0),
        Q("Ce permite cuvântul 'virtual' în C++?", listOf("polimorfism", "multithreading", "template-uri", "namespace-uri"), 0),
        Q("Ce este un destructor?", listOf("O funcție apelată la crearea obiectului", "O funcție apelată la distrugerea obiectului", "Un tip de pointer", "O metodă statică"), 1),
        Q("Ce cuvânt cheie interzice modificarea unei variabile?", listOf("static", "virtual", "const", "mutable"), 2),
        Q("Ce face 'delete p;'?", listOf("Șterge fișierul 'p'", "Eliberează memoria alocată cu 'new' indicată de 'p'", "Setează 'p' la null", "Oprește programul"), 1),
        Q("Cum se accesează un membru al unui obiect printr-un pointer 'ptr'?", listOf("ptr.membru", "ptr->membru", "ptr::membru", "ptr[membru]"), 1),
        Q("Ce este supraîncărcarea funcțiilor (function overloading)?", listOf("Definirea mai multor funcții cu același nume dar parametri diferiți", "Definirea unei funcții într-o clasă derivată", "Folosirea pointerilor la funcții", "Crearea unei funcții virtuale"), 0),
        Q("Ce specificator de acces face un membru al clasei accesibil doar din interiorul clasei?", listOf("public", "protected", "private", "internal"), 2)
    )

    private val roCppHard = listOf(
        Q("Care e complexitatea medie a lui std::sort?", listOf("O(n)", "O(n log n)", "O(log n)", "O(n^2)"), 1),
        Q("Ce container păstrează cheile ordonate?", listOf("std::map", "std::unordered_map", "std::vector", "std::queue"), 0),
        Q("Ce este RAII?", listOf("O tehnică de management al erorilor", "Un tip de container STL", "Un idiom pentru managementul resurselor (ex: memorie, fișiere)", "O metodă de sortare"), 2),
        Q("Ce tip de smart pointer *nu* permite copierea?", listOf("std::shared_ptr", "std::weak_ptr", "std::unique_ptr", "std::auto_ptr"), 2), // std::auto_ptr e învechit, dar întrebarea rămâne validă
        Q("Ce face 'std::move'?", listOf("Mută fizic datele în memorie", "Copiază o resursă", "Convertește o expresie în rvalue reference", "Șterge o variabilă"), 2),
        Q("Cum se definește un template pentru o funcție care primește un argument?", listOf("template<T> function(T arg)", "function<T>(T arg)", "template<class T> void func(T arg)", "class<T> func(T arg)"), 2), // Presupunând că funcția se numește 'func' și e 'void'
        Q("Ce container oferă timp de căutare mediu O(1) (bazat pe hash)?", listOf("std::map", "std::vector", "std::set", "std::unordered_map"), 3),
        Q("Ce înseamnă 'rvalue reference' (&&)?", listOf("O referință la o valoare constantă", "O referință la o valoare temporară sau mutabilă", "Un pointer la o funcție", "Un alias pentru o altă variabilă"), 1)
    )

    // Java
    private val roJavaEasy = listOf(
        Q("Ce cuvânt definește o clasă în Java?", listOf("class", "struct", "object", "def"), 0),
        Q("Ce metodă e punctul de intrare în Java?", listOf("start()", "run()", "main()", "init()"), 2),
        Q("Cum se afișează 'Text' pe o linie nouă?", listOf("System.out.println(\"Text\");", "print(\"Text\");", "cout << \"Text\";", "console.log(\"Text\");"), 0),
        Q("Ce tip de dată se folosește pentru numere întregi?", listOf("String", "float", "int", "boolean"), 2),
        Q("Ce operator se folosește pentru a crea o instanță nouă a unei clase?", listOf("new", "create", "alloc", "instance"), 0),
        Q("Cum se declară un comentariu pe o singură linie?", listOf("/* comment */", "# comment", "// comment", ""), 2),
        Q("Ce valoare are o variabilă booleană care e 'adevărat'?", listOf("true", "1", "yes", "T"), 0),
        Q("Care este extensia fișierelor sursă Java?", listOf(".class", ".jvm", ".java", ".jar"), 2)
    )

    private val roJavaMedium = listOf(
        Q("Ce colecție nu permite duplicate?", listOf("List", "Set", "ArrayList", "Queue"), 1),
        Q("Ce înseamnă 'static' pe o metodă?", listOf("aparține instanței", "este finală", "este abstractă", "aparține clasei, nu instanței"), 3), // Am reformulat răspunsul corect pentru claritate
        Q("Ce cuvânt cheie este folosit pentru a preveni moștenirea unei clase?", listOf("final", "static", "abstract", "private"), 0),
        Q("Cum se compară corect două obiecte String ('s1' și 's2') pentru egalitatea conținutului?", listOf("s1 == s2", "s1.equals(s2)", "s1.compare(s2)", "compare(s1, s2)"), 1),
        Q("Ce este un constructor?", listOf("O metodă pentru a distruge obiecte", "O metodă specială apelată la crearea unui obiect", "Un tip de variabilă", "O colecție de date"), 1),
        Q("Ce cuvânt cheie indică faptul că o metodă *trebuie* să fie suprascrisă de subclase?", listOf("virtual", "override", "abstract", "static"), 2),
        Q("Care pachet este importat automat în fiecare clasă Java?", listOf("java.util", "java.io", "java.system", "java.lang"), 3),
        Q("Ce cuvânt cheie se folosește pentru a referi instanța curentă a clasei?", listOf("self", "this", "me", "current"), 1)
    )

    private val roJavaHard = listOf(
        Q("Ce interfață se folosește la lambda în Java 8?", listOf("Functional interface", "Runnable", "Serializable", "AutoCloseable"), 0),
        Q("Ce cuvânt se folosește pentru moștenire?", listOf("inherits", "extends", "implements", "super"), 1), // Atenție: 'implements' e pt interfețe, 'extends' pt clase
        Q("Ce se întâmplă în blocul 'finally' al unui 'try-catch'?", listOf("Se execută doar dacă e o excepție", "Se execută doar dacă nu e excepție", "Se execută întotdeauna (dacă s-a intrat în try)", "Este opțional și se execută la finalul programului"), 2),
        Q("Ce colecție folosește o structură de tip 'key-value'?", listOf("ArrayList", "HashSet", "HashMap", "LinkedList"), 2),
        Q("Ce face cuvântul cheie 'synchronized'?", listOf("Sincronizează datele cu un cloud", "Asigură că doar un thread execută un bloc de cod la un moment dat", "Face variabila nemodificabilă", "Importă o bibliotecă"), 1),
        Q("Care este diferența principală între o clasă abstractă și o interfață (post-Java 8)?", listOf("Interfața nu poate avea variabile", "Clasa abstractă poate avea constructori", "Interfața nu poate avea metode statice", "Nu există nicio diferență"), 1), // O interfață NU poate avea constructori
        Q("Ce este Garbage Collector (GC)?", listOf("Un program care șterge fișiere vechi", "Un proces care eliberează automat memoria nefolosită", "Un tool de formatare a codului", "O metodă de a prinde erori"), 1),
        Q("Ce face cuvântul cheie 'volatile'?", listOf("Face variabila serializabilă", "Oprește programul", "Face variabila nemodificabilă (const)", "Asigură vizibilitatea modificărilor variabilei între thread-uri"), 3)
    )

    // Python
    private val roPythonEasy = listOf(
        Q("Ce simbol începe un comentariu în Python?", listOf("#", "//", "/* */", "--"), 0),
        Q("Cum afișezi în Python 3?", listOf("echo()", "print()", "printf()", "cout <<"), 1),
        Q("Ce operator se folosește pentru exponențiere (ridicare la putere)?", listOf("^", "**", "*", "pow()"), 1),
        Q("Care este operatorul de atribuire?", listOf("==", "=", ":=", "->"), 1),
        Q("Ce tip de dată este 'True'?", listOf("int", "string", "float", "bool"), 3),
        Q("Cum obții input de la utilizator?", listOf("input()", "read()", "cin >>", "scan()"), 0),
        Q("Ce cuvânt cheie începe o instrucțiune 'if'?", listOf("then", "if", "case", "when"), 1),
        Q("Cum se compară 'nu este egal'?", listOf("!=", "<>", "==", "NOT"), 0)
    )

    private val roPythonMedium = listOf(
        Q("Care e un literal de listă corect?", listOf("{1,2,3}", "(1,2,3)", "[1,2,3]", "<1,2,3>"), 2),
        Q("Cum definești o funcție?", listOf("func f()", "def f()", "function f()", "fn f()"), 1),
        Q("Ce tip de dată este imutabil (nu poate fi modificat)?", listOf("list", "dict", "set", "tuple"), 3),
        Q("Cum se accesează primul element al unei liste 'L'?", listOf("L[1]", "L.first()", "L[0]", "L.get(0)"), 2),
        Q("Ce cuvânt cheie se folosește pentru a crea un ciclu 'for'?", listOf("for", "loop", "while", "repeat"), 0),
        Q("Ce metodă adaugă un element la sfârșitul unei liste 'L'?", listOf("L.add()", "L.push()", "L.append()", "L.insert()"), 2),
        Q("Cum se creează un dicționar (dict)?", listOf("[1:'a']", "(1:'a')", "{'a': 1}", "<'a': 1>"), 2),
        Q("Ce cuvânt cheie se folosește pentru a importa un modul?", listOf("import", "include", "using", "require"), 0)
    )

    private val roPythonHard = listOf(
        Q("Ce cuvânt se folosește la tratarea excepțiilor (prinderea erorii)?", listOf("catch", "handle", "try", "except"), 3), // Am reformulat întrebarea
        Q("Ce întoarce 'len()'?", listOf("tipul", "lungimea", "adresa", "hash-ul"), 1),
        Q("Ce este o 'list comprehension'?", listOf("O metodă de a sorta o listă", "Un mod concis de a crea liste", "Un tip de eroare în liste", "O funcție care unește liste"), 1),
        Q("Ce cuvânt cheie creează o funcție anonimă (mică)?", listOf("def", "func", "lambda", "anon"), 2),
        Q("Ce face 's.split(sep)' pentru un string 's'?", listOf("Unește string-uri", "Taie string-ul în bucăți la 'sep' și returnează o listă", "Verifică dacă 's' conține 'sep'", "Înlocuiește 'sep' în 's'"), 1),
        Q("Cum se deschide un fișier 'f.txt' pentru citire?", listOf("open('f.txt', 'r')", "open('f.txt', 'w')", "read('f.txt')", "open.read('f.txt')"), 0),
        Q("Ce face 'yield' într-o funcție?", listOf("Oprește funcția definitiv", "Returnează o valoare și pune funcția în pauză (creează un generator)", "Aruncă o excepție", "Printează o valoare"), 1),
        Q("Ce operator se folosește pentru 'identity check' (verifică dacă sunt același obiect)?", listOf("==", "is", "equals", "id()"), 1)
    )

    // German -> deocamdată copiem engleza
    // --- C++ (Deutsch) ---

    private val deCppEasy = listOf(
        Q("Welcher Header wird für `cout` benötigt?", listOf("<iostream>", "<stdio.h>", "<vector>", "<string>"), 0),
        Q("Was beginnt mit `//`?", listOf("Eine Klasse", "Ein einzeiliger Kommentar", "Eine Schleife", "Ein Namespace"), 1),
        Q("Wie deklariert man eine Ganzzahl-Variable namens 'var'?", listOf("int var;", "variable int var;", "var int;", "integer var;"), 0),
        Q("Welcher Operator prüft auf Gleichheit?", listOf("=", "==", "!=", "=>"), 1),
        Q("Wie liest man einen Wert von der Tastatur in die Variable 'x' ein?", listOf("read(x);", "cin >> x;", "cout << x;", "input(x);"), 1)
    )

    private val deCppMedium = listOf(
        Q("Welches Schlüsselwort allokiert Speicher auf dem Heap?", listOf("new", "malloc", "alloc", "create"), 0),
        Q("Was ermöglicht das Schlüsselwort 'virtual' in C++?", listOf("Polymorphismus", "Multithreading", "Templates", "Namespaces"), 0),
        Q("Was ist ein Destruktor?", listOf("Eine Funktion beim Erstellen eines Objekts", "Eine Funktion beim Zerstören eines Objekts", "Ein Zeigertyp", "Eine statische Methode"), 1),
        Q("Welches Schlüsselwort verhindert die Änderung einer Variable?", listOf("static", "virtual", "const", "mutable"), 2),
        Q("Wie greift man über einen Zeiger 'ptr' auf ein Objektmitglied zu?", listOf("ptr.mitglied", "ptr->mitglied", "ptr::mitglied", "ptr[mitglied]"), 1)
    )

    private val deCppHard = listOf(
        Q("Was ist die durchschnittliche Komplexität von `std::sort`?", listOf("O(n)", "O(n log n)", "O(log n)", "O(n^2)"), 1),
        Q("Welcher Container speichert Schlüssel sortiert?", listOf("std::map", "std::unordered_map", "std::vector", "std::queue"), 0),
        Q("Was bedeutet RAII (Resource Acquisition Is Initialization)?", listOf("Eine Fehlerbehandlungstechnik", "Ein STL-Containertyp", "Ein Idiom zur Ressourcenverwaltung (z.B. Speicher)", "Eine Sortiermethode"), 2),
        Q("Welcher Smart Pointer erlaubt *kein* Kopieren?", listOf("std::shared_ptr", "std::weak_ptr", "std::unique_ptr", "std::auto_ptr"), 2),
        Q("Was macht `std::move`?", listOf("Verschiebt Daten physisch im Speicher", "Kopiert eine Ressource", "Wandelt einen Ausdruck in eine Rvalue-Referenz um", "Löscht eine Variable"), 2)
    )

// --- Java (Deutsch) ---

    private val deJavaEasy = listOf(
        Q("Welches Wort definiert eine Klasse in Java?", listOf("class", "struct", "object", "def"), 0),
        Q("Welche Methode ist der Einstiegspunkt (entry point) in Java?", listOf("start()", "run()", "main()", "init()"), 2),
        Q("Wie gibt man 'Text' auf einer neuen Zeile aus?", listOf("System.out.println(\"Text\");", "print(\"Text\");", "cout << \"Text\";", "console.log(\"Text\");"), 0),
        Q("Welcher Datentyp wird für ganze Zahlen verwendet?", listOf("String", "float", "int", "boolean"), 2),
        Q("Welcher Operator erstellt eine neue Instanz einer Klasse?", listOf("new", "create", "alloc", "instance"), 0)
    )

    private val deJavaMedium = listOf(
        Q("Welche Collection (Sammlung) erlaubt keine Duplikate?", listOf("List", "Set", "ArrayList", "Queue"), 1),
        Q("Was bedeutet 'static' bei einer Methode?", listOf("Sie gehört zur Instanz", "Sie ist final", "Sie ist abstrakt", "Sie gehört zur Klasse, nicht zur Instanz"), 3),
        Q("Welches Schlüsselwort verhindert das Erben (Vererbung) einer Klasse?", listOf("final", "static", "abstract", "private"), 0),
        Q("Wie vergleicht man zwei Strings ('s1', 's2') korrekt auf Inhaltsgleichheit?", listOf("s1 == s2", "s1.equals(s2)", "s1.compare(s2)", "compare(s1, s2)"), 1),
        Q("Was ist ein Konstruktor?", listOf("Eine Methode zum Zerstören von Objekten", "Eine spezielle Methode, die beim Erstellen eines Objekts aufgerufen wird", "Ein Variablentyp", "Eine Datensammlung"), 1)
    )

    private val deJavaHard = listOf(
        Q("Welcher Interfacetyp wird für Lambdas (ab Java 8) verwendet?", listOf("Functional Interface (Funktionales Interface)", "Runnable", "Serializable", "AutoCloseable"), 0),
        Q("Welches Wort wird für Vererbung (von Klassen) verwendet?", listOf("inherits", "extends", "implements", "super"), 1),
        Q("Was passiert im 'finally'-Block eines 'try-catch'?", listOf("Wird nur ausgeführt, wenn eine Ausnahme auftritt", "Wird nur ausgeführt, wenn keine Ausnahme auftritt", "Wird (fast) immer ausgeführt, wenn der 'try'-Block betreten wurde", "Ist optional und wird am Ende des Programms ausgeführt"), 2),
        Q("Welche Collection nutzt eine 'key-value' (Schlüssel-Wert) Struktur?", listOf("ArrayList", "HashSet", "HashMap", "LinkedList"), 2),
        Q("Was bewirkt das Schlüsselwort 'synchronized'?", listOf("Synchronisiert Daten mit einer Cloud", "Stellt sicher, dass nur ein Thread den Codeblock gleichzeitig ausführt", "Macht die Variable unveränderlich", "Importiert eine Bibliothek"), 1)
    )

// --- Python (Deutsch) ---

    private val dePythonEasy = listOf(
        Q("Welches Symbol startet einen Kommentar in Python?", listOf("#", "//", "/* */", "--"), 0),
        Q("Wie gibt man in Python 3 etwas auf der Konsole aus?", listOf("echo()", "print()", "printf()", "cout <<"), 1),
        Q("Welcher Operator wird für die Potenzierung (z.B. 2 hoch 3) verwendet?", listOf("^", "**", "*", "pow()"), 1),
        Q("Welches ist der Zuweisungsoperator (assignment)?", listOf("==", "=", ":=", "->"), 1),
        Q("Welchen Datentyp hat der Wert 'True'?", listOf("int", "string", "float", "bool"), 3)
    )

    private val dePythonMedium = listOf(
        Q("Was ist ein korrektes Listen-Literal (list literal)?", listOf("{1,2,3}", "(1,2,3)", "[1,2,3]", "<1,2,3>"), 2),
        Q("Wie definiert man eine Funktion?", listOf("func f():", "def f():", "function f():", "fn f():"), 1),
        Q("Welcher Datentyp ist 'immutable' (unveränderlich)?", listOf("list", "dict", "set", "tuple"), 3),
        Q("Wie greift man auf das erste Element einer Liste 'L' zu?", listOf("L[1]", "L.first()", "L[0]", "L.get(0)"), 2),
        Q("Welche Methode fügt ein Element am Ende einer Liste 'L' hinzu?", listOf("L.add()", "L.push()", "L.append()", "L.insert()"), 2)
    )

    private val dePythonHard = listOf(
        Q("Welches Wort wird bei der Ausnahmebehandlung (exception handling) zum *Abfangen* eines Fehlers verwendet?", listOf("catch", "handle", "try", "except"), 3),
        Q("Was gibt die Funktion `len(obj)` zurück?", listOf("Den Typ von obj", "Die Länge oder Anzahl der Elemente von obj", "Die Speicheradresse von obj", "Den Hash-Wert von obj"), 1),
        Q("Was ist eine 'List Comprehension'?", listOf("Eine Methode, um eine Liste zu sortieren", "Eine kompakte (kurze) Syntax, um Listen zu erstellen", "Ein Fehler beim Erstellen einer Liste", "Eine Funktion, die Listen zusammenführt"), 1),
        Q("Welches Schlüsselwort erstellt eine (kleine) anonyme Funktion?", listOf("def", "func", "lambda", "anon"), 2),
        Q("Was macht `s.split(sep)` bei einem String 's'?", listOf("Verbindet Strings", "Trennt 's' am Trennzeichen 'sep' und gibt eine Liste zurück", "Prüft, ob 's' 'sep' enthält", "Ersetzt 'sep' in 's'"), 1)
    )

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
    private var correctCount = 0


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
            val fade = AnimationUtils.loadAnimation(this, R.anim.fade_out)
            questionText.startAnimation(fade)
            answerButtons.forEach { it.startAnimation(fade) }
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
//        if (currentQuestionIndex >= questions.size) {
//
//            val repo = com.example.aplicatie.data.UserRepository()
//            repo.updateAfterQuiz(
//                username = currentUsername,
//                lastScore = score,
//                correctCount = correctCount,
//                totalQuestions = questions.size
//            )
//
//            // streak
//            com.example.aplicatie.util.StreakManager.onQuizFinished(this, currentUsername)
//
//
//            val intent = Intent(this, ResultActivity::class.java)
//            intent.putExtra("score", score)
//            intent.putStringArrayListExtra("wrongQuestions", ArrayList(wrongQuestions))
//            intent.putStringArrayListExtra("wrongCorrectAnswers", ArrayList(wrongCorrectAnswers))
//            startActivity(intent)
//            finish()
//            return
//        }
        if (currentQuestionIndex >= questions.size) {

            UserRepository().updateHighScore(currentUsername, score)
            // actualizare Streak
            com.example.aplicatie.util.StreakManager.onQuizFinished(this, currentUsername)

            // 🔔 trimitem un broadcast custom când s-a terminat quiz-ul
            val bcast = Intent(com.example.aplicatie.util.QuizFinishedReceiver.ACTION_QUIZ_FINISHED).apply {
                putExtra("username", currentUsername)
                putExtra("score", score)
            }
            sendBroadcast(bcast)

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

        // 1) animație de intrare
        val slideIn = AnimationUtils.loadAnimation(this, R.anim.slide_in_left)

        // setezi textul întrebării
        questionText.text = question.text
        questionText.startAnimation(slideIn)

        // pentru fiecare răspuns
        answerButtons.forEachIndexed { index, button ->
            button.setBackgroundResource(R.drawable.r_a_b)
            button.setTextColor(Color.BLACK)
            button.isClickable = true
            button.text = question.options[index]
            button.startAnimation(slideIn)   // <- și butonul intră frumos

            button.setOnClickListener {
                timer.cancel()
                val timeTaken = SystemClock.elapsedRealtime() - startTime

                if (index == question.correctAnswerIndex) {
                    correctCount++
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

                // dezactivezi restul
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

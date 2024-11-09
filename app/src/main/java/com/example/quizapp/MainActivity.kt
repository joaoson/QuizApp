package com.example.quizapp

import CategorySelectionScreen
import QuizScreen
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.Room
import com.example.quizapp.ui.theme.QuizAppTheme
import com.example.quizapp.scienceQuestions
import com.example.quizapp.historyQuestions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.platform.LocalSoftwareKeyboardController



class MainActivity : ComponentActivity() {
    private lateinit var leaderboardDao: LeaderboardDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = QuizDatabase.getDatabase(this)
        val leaderboardDao = db.leaderboardDao()
        setContent {
            QuizAppTheme {
                QuizApp(leaderboardDao)
            }
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun QuizApp(leaderboardDao: LeaderboardDao) {
    var nickname by remember { mutableStateOf("") }
    var isNicknameEntered by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf<List<Question>?>(null) }
    var currentQuestionIndex by remember { mutableStateOf(0) }
    var score by remember { mutableStateOf(0) }
    val scope = rememberCoroutineScope()
    var selectedCategoryName by remember { mutableStateOf<String?>(null) }
    var entries by remember { mutableStateOf(listOf<LeaderboardEntry>()) }
    var showNextQuestion by remember { mutableStateOf(false) }
    var showIntro by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        scope.launch(Dispatchers.IO) {
            entries = leaderboardDao.getAllEntries()
        }
    }

    fun resetQuiz() {
        nickname = ""
        isNicknameEntered = false
        selectedCategory = null
        currentQuestionIndex = 0
        score = 0
        selectedCategoryName = null
        showNextQuestion = false
        showIntro = false
    }

    if (showIntro) {
        IntroductionScreen(onStart = { showIntro = false })
    } else if (!isNicknameEntered) {
        NicknameScreen(nickname = nickname, onNicknameChange = {
            nickname = it
        }, onStartQuiz = {
            if (nickname.isNotBlank()) {
                isNicknameEntered = true
            }
        })
    } else if (selectedCategory == null) {
        CategorySelectionScreen(onCategorySelected = { categoryName, categoryQuestions ->
            selectedCategoryName = categoryName
            selectedCategory = categoryQuestions.shuffled()
        })
    } else if (currentQuestionIndex < selectedCategory!!.size) {
        QuizScreen(
            question = selectedCategory!![currentQuestionIndex],
            onAnswerSelected = { isCorrect ->
                if (isCorrect) score++
                showNextQuestion = true
            }
        )

        LaunchedEffect(showNextQuestion) {
            if (showNextQuestion) {
                delay(2000)
                currentQuestionIndex++
                showNextQuestion = false
            }
        }
    } else {
        val finalRegistry = LeaderboardEntry(
            nickname = nickname,
            category = selectedCategoryName ?: "Categoria desconhecida",
            score = score,
            timeTaken = 0
        )

        CoroutineScope(Dispatchers.IO).launch {
            leaderboardDao.insertEntry(finalRegistry)
        }
        ResultScreen(
            score = score,
            totalQuestions = selectedCategory!!.size,
            nickname = nickname,
            category = selectedCategoryName ?: "Categoria desconhecida",
            leaderboardDao = leaderboardDao,
            onRetry = { resetQuiz() }
        )
    }
}

@Composable
fun IntroductionScreen(onStart: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logobackground),
            contentDescription = "App Logo",
            modifier = Modifier.size(200.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "CyberQuiz",
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Blue
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Welcome to CyberQuiz, the app where you can compete with your friends on multiple topics.",
            fontSize = 20.sp,
            modifier = Modifier.padding(16.dp),
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = onStart) {
            Text(text = "Get Started")
        }
    }
}


@Composable
fun NicknameScreen(nickname: String, onNicknameChange: (String) -> Unit, onStartQuiz: () -> Unit) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Digite seu apelido:", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = nickname,
            onValueChange = onNicknameChange,
            label = { Text("Apelido") },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                if (nickname.isNotBlank()) {
                    onStartQuiz()
                    keyboardController?.hide()
                }
            })
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onStartQuiz) {
            Text(text = "Iniciar Quiz")
        }
    }
}

package com.example.quizapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    var showNextQuestion by remember { mutableStateOf(false) } // Flag to control delay

    LaunchedEffect(Unit) {
        scope.launch(Dispatchers.IO) {
            entries = leaderboardDao.getAllEntries()
        }
    }

    if (!isNicknameEntered) {
        NicknameScreen(nickname = nickname, onNicknameChange = {
            nickname = it
        }, onStartQuiz = {
            if (nickname.isNotBlank()) {
                isNicknameEntered = true
            }
        })
    } else if (selectedCategory == null) {
        CategorySelectionScreen(onCategorySelected = { categoryName,categoryQuestions ->
            selectedCategoryName = categoryName
            selectedCategory = categoryQuestions.shuffled()
        })
    } else if (currentQuestionIndex < selectedCategory!!.size) {
        QuizScreen(
            question = selectedCategory!![currentQuestionIndex],
            onAnswerSelected = { isCorrect ->
                if (isCorrect) score++
                showNextQuestion = true // Set flag to trigger delay
            }
        )

        // Handle delay before moving to the next question
        LaunchedEffect(showNextQuestion) {
            if (showNextQuestion) {
                delay(2000) // Wait 2 seconds
                currentQuestionIndex++ // Move to next question
                showNextQuestion = false // Reset flag
            }
        }
    } else {
        val finalRegistry = LeaderboardEntry(
            nickname = nickname,
            category = selectedCategoryName ?: "Categoria desconhecida",
            score = score,
            timeTaken = 0
        )

        // Executa a operação de inserção em uma coroutine com o dispatcher de IO
        CoroutineScope(Dispatchers.IO).launch {
            leaderboardDao.insertEntry(finalRegistry)
        }
        ResultScreen(
            score = score,
            totalQuestions = selectedCategory!!.size,
            nickname = nickname,
            category = selectedCategoryName ?: "Categoria desconhecida",
            leaderboardDao = leaderboardDao
        )
    }
}


@Composable
fun NicknameScreen(nickname: String, onNicknameChange: (String) -> Unit, onStartQuiz: () -> Unit) {
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
            label = { Text("Apelido") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onStartQuiz) {
            Text(text = "Iniciar Quiz")
        }
    }
}

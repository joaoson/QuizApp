package com.example.quizapp

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun QuizScreen(
    question: Question,
    onAnswerSelected: (Boolean) -> Unit
) {
    var selectedAnswer by remember { mutableStateOf<String?>(null) }
    var startTime by remember { mutableStateOf(System.currentTimeMillis()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Display the question and options
        Text(text = question.questionText, fontSize = 20.sp)
        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = painterResource(id = question.imageResId),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentScale = ContentScale.Crop
        )

        question.options.forEach { answer ->
            val isCorrect = answer == question.correctAnswer
            val buttonColor by animateColorAsState(
                targetValue = when {
                    selectedAnswer == null -> Color.Gray
                    answer == question.correctAnswer -> Color.Green
                    answer == selectedAnswer -> Color.Red
                    else -> Color.Gray
                }
            )

            Button(
                onClick = {
                    selectedAnswer = answer
                    val timeTaken = System.currentTimeMillis() - startTime
                    onAnswerSelected(isCorrect)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(buttonColor)
            ) {
                Text(text = answer)
            }
        }

        // Delay and reset selected answer after 2 seconds if one is selected
        if (selectedAnswer != null) {
            LaunchedEffect(selectedAnswer) {
                delay(2000)
                selectedAnswer = null // Reset selected answer
                startTime = System.currentTimeMillis() // Reset the start time for the next question
                // Trigger the next question logic here, for example:
                // onNextQuestion() // If you have this function to load the next question
            }
        }
    }
}

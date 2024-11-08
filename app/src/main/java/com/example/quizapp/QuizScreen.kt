import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import android.media.MediaPlayer
import androidx.compose.animation.animateColorAsState
import com.example.quizapp.Question
import com.example.quizapp.R

@Composable
fun QuizScreen(
    question: Question,
    onAnswerSelected: (Boolean) -> Unit
) {
    val context = LocalContext.current
    var selectedAnswer by remember { mutableStateOf<String?>(null) }
    var startTime by remember { mutableStateOf(System.currentTimeMillis()) }
    var isQuestionVisible by remember { mutableStateOf(false) }
    var isImageVisible by remember { mutableStateOf(false) }
    var optionVisibilityStates by remember { mutableStateOf(List(question.options.size) { false }) }

    // MediaPlayers for correct and wrong sounds
    val correctSound = remember { MediaPlayer.create(context, R.raw.correctsound) }
    val wrongSound = remember { MediaPlayer.create(context, R.raw.wrongsound) }

    // Trigger animation whenever a new question is loaded
    LaunchedEffect(question) {
        isQuestionVisible = false
        isImageVisible = false
        optionVisibilityStates = List(question.options.size) { false }

        // Animate question text and image first, then options one by one
        isQuestionVisible = true
        delay(500) // Delay for image to appear after question
        isImageVisible = true
        question.options.indices.forEach { index ->
            delay(300) // Delay between each option appearance
            optionVisibilityStates = optionVisibilityStates.toMutableList().apply { this[index] = true }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Animate the question text
        AnimatedVisibility(
            visible = isQuestionVisible,
            enter = fadeIn() + slideInHorizontally(initialOffsetX = { -50 })
        ) {
            Text(text = question.questionText, fontSize = 20.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Animate the image
        AnimatedVisibility(
            visible = isImageVisible,
            enter = fadeIn() + slideInHorizontally(initialOffsetX = { -50 })
        ) {
            Image(
                painter = painterResource(id = question.imageResId),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Animate each answer option with alternating slide directions
        question.options.forEachIndexed { index, answer ->
            AnimatedVisibility(
                visible = optionVisibilityStates[index],
                enter = fadeIn() + slideInHorizontally(initialOffsetX = { if (index % 2 == 0) -300 else 300 })
            ) {
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

                        // Play sound based on answer correctness
                        if (isCorrect) {
                            correctSound.start()
                        } else {
                            wrongSound.start()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(buttonColor)
                ) {
                    Text(text = answer)
                }
            }
        }

        // Delay and reset selected answer after 2 seconds if one is selected
        if (selectedAnswer != null) {
            LaunchedEffect(selectedAnswer) {
                delay(2000)
                selectedAnswer = null
                startTime = System.currentTimeMillis()
            }
        }
    }

    // Release MediaPlayers when the composable is disposed
    DisposableEffect(Unit) {
        onDispose {
            correctSound.release()
            wrongSound.release()
        }
    }
}

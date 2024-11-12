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
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.ui.text.style.TextAlign
import com.example.quizapp.Question
import com.example.quizapp.R

@Composable
fun QuizScreen(
    question: Question,
    score: Int,
    onAnswerSelected: (Boolean, Int) -> Unit
) {
    val context = LocalContext.current
    var selectedAnswer by remember { mutableStateOf<String?>(null) }
    var startTime by remember { mutableStateOf(System.currentTimeMillis()) }
    var isQuestionVisible by remember { mutableStateOf(false) }
    var isImageVisible by remember { mutableStateOf(false) }
    var optionVisibilityStates by remember { mutableStateOf(List(question.options.size) { false }) }

    var shuffledOptions by remember { mutableStateOf(question.options.shuffled()) }

    val correctSound = remember { MediaPlayer.create(context, R.raw.correctsound) }
    val wrongSound = remember { MediaPlayer.create(context, R.raw.wrongsound) }
    val buzzerSound = remember { MediaPlayer.create(context, R.raw.buzzer) }

    var timerProgress by remember { mutableStateOf(1f) }

    val timerWidth by animateDpAsState(targetValue = 400.dp * timerProgress)

    fun calculateScore(timeTaken: Long): Int {
        val secondsTaken = timeTaken / 1000.0 // Convert to seconds
        return when {
            secondsTaken <= 1 -> 100    // 1 second or less: 100 points
            secondsTaken <= 2 -> 80     // 1-2 seconds: 80 points
            secondsTaken <= 3 -> 60     // 2-3 seconds: 60 points
            secondsTaken <= 4 -> 40     // 3-4 seconds: 40 points
            secondsTaken <= 5 -> 20     // 4-5 seconds: 20 points
            else -> 10                  // More than 5 seconds: 10 points
        }
    }

    LaunchedEffect(question) {
        isQuestionVisible = false
        isImageVisible = false
        optionVisibilityStates = List(question.options.size) { false }

        shuffledOptions = question.options.shuffled()

        isQuestionVisible = true
        delay(500)
        isImageVisible = true
        shuffledOptions.indices.forEach { index ->
            delay(300)
            optionVisibilityStates = optionVisibilityStates.toMutableList().apply { this[index] = true }
        }
    }

    LaunchedEffect(key1 = question) {
        timerProgress = 1f
        delay(2000L)

        val totalTime = 10000L // 7 seconds
        val interval = 50L

        for (timeLeft in totalTime downTo 0 step interval) {
            delay(interval)
            timerProgress = timeLeft / totalTime.toFloat()
        }
        if (selectedAnswer == null) {
            buzzerSound.start()
            onAnswerSelected(false, 0)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Score: $score",
            fontSize = 24.sp,
            color = Color.White,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(16.dp))


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .background(Color.Gray.copy(alpha = 0.3f))
        ) {
            Box(
                modifier = Modifier
                    .height(8.dp)
                    .width(timerWidth)
                    .background(Color.Red)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        AnimatedVisibility(
            visible = isQuestionVisible,
            enter = fadeIn() + slideInHorizontally(initialOffsetX = { -50 })
        ) {
            Text(text = question.questionText, fontSize = 20.sp, textAlign = TextAlign.Center, color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

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

        shuffledOptions.forEachIndexed { index, answer ->
            AnimatedVisibility(
                visible = optionVisibilityStates[index],
                enter = fadeIn() + slideInHorizontally(initialOffsetX = { if (index % 2 == 0) -300 else 300 })
            ) {
                val isCorrect = answer == question.correctAnswer
                val buttonColor by animateColorAsState(
                    targetValue = when {
                        selectedAnswer == null -> Color(0xFFADD8E6)  // Light blue
                        answer == question.correctAnswer -> Color.Green
                        answer == selectedAnswer -> Color.Red
                        else -> Color(0xFFADD8E6)  // Light blue
                    }
                )

                Button(
                    onClick = {
                        selectedAnswer = answer
                        val timeTaken = System.currentTimeMillis() - startTime
                        val score = if (isCorrect) calculateScore(timeTaken) else 0

                        onAnswerSelected(isCorrect,score)

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
                    Text(text = answer,color = Color.Black)
                }
            }
        }

        if (selectedAnswer != null) {
            LaunchedEffect(selectedAnswer) {
                delay(1900)
                selectedAnswer = null
                startTime = System.currentTimeMillis()
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            correctSound.release()
            wrongSound.release()
            buzzerSound.release()
        }
    }
}

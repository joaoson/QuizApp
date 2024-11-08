package com.example.quizapp

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun ResultScreen(
    score: Int,
    totalQuestions: Int,
    nickname: String,
    category: String,
    leaderboardDao: LeaderboardDao
) {
    var leaderboardEntries by remember { mutableStateOf<List<LeaderboardEntry>>(emptyList()) }

    // Fetch leaderboard entries from the database on the IO thread
    LaunchedEffect(Unit) {
        val entries = leaderboardDao.getTop10EntriesByCategory(category)
        leaderboardEntries = entries.take(10) // Get top 10 entries
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Você concluiu o quiz!", fontSize = 28.sp)
        Text(text = "Pontuação: $score de $totalQuestions", fontSize = 20.sp)

        Spacer(modifier = Modifier.height(16.dp))

        // Leaderboard Section
        Text(text = "Leaderboard", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(8.dp))

        // Display the top 10 leaderboard entries
        leaderboardEntries.forEachIndexed { index, entry ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "${index + 1}. ${entry.nickname}", fontSize = 18.sp)
                Text(text = "Score: ${entry.score}", fontSize = 18.sp)
            }
            Spacer(modifier = Modifier.height(4.dp))
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = {  }) {
            Text(text = "Tentar novamente")
        }
    }
}

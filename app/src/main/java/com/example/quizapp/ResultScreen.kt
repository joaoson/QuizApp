package com.example.quizapp

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ResultScreen(score: Int, totalQuestions: Int,nickname: String,
                 category: String,
                 leaderboardDao: LeaderboardDao) {
    var leaderboardEntries by remember { mutableStateOf<List<LeaderboardEntry>>(emptyList()) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Você concluiu o quiz!", fontSize = 28.sp)
        Text(text = "Pontuação: $score de $totalQuestions", fontSize = 20.sp)
        Button(onClick = { /* Reinicie o quiz ou navegue para outra tela */ }) {
            Text(text = "Tentar novamente")

        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Leaderboard", fontSize = 24.sp)
        if (category != null) {
            Text(text = "Categoria: $category", fontSize = 20.sp)
        }
        leaderboardEntries.forEach { entry ->
            Text(text = "${entry.nickname} - Pontos: ${entry.score} - Tempo: ${entry.timeTaken}ms - Categoria: ${entry.category}")
        }
    }
}

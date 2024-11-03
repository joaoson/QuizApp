package com.example.quizapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "leaderboard")
data class LeaderboardEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nickname: String,
    val category: String, // Novo campo para a categoria
    val score: Int,
    val timeTaken: Long // Tempo de resposta em milissegundos
)

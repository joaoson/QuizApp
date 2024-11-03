package com.example.quizapp

data class Question(
    val questionText: String,
    val imageResId: Int,  // Alterado para Int para representar corretamente um ID de recurso.
    val options: List<String>,
    val correctAnswer: String
)

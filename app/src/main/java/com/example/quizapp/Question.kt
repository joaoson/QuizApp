package com.example.quizapp

data class Question(
    val questionText: String,
    val imageResId: Int?= null,
    val options: List<String>,
    val correctAnswer: String,
    val songResId: Int?= null
)

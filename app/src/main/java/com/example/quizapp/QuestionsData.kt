package com.example.quizapp

val scienceQuestions = listOf(
    Question(
        questionText = "Qual é o elemento químico com símbolo 'O'?",
        imageResId = R.drawable.paris,
        options = listOf("Oxigênio", "Hidrogênio", "Nitrogênio", "Carbono"),
        correctAnswer = "Oxigênio"
    ),
    Question(
        questionText = "Qual é a fórmula química da água?",
        imageResId = R.drawable.paris,
        options = listOf("H2O", "CO2", "O2", "NaCl"),
        correctAnswer = "H2O"
    )
)

val historyQuestions = listOf(
    Question(
        questionText = "Quem foi o primeiro presidente dos Estados Unidos?",
        imageResId = R.drawable.paris,
        options = listOf("George Washington", "Abraham Lincoln", "Thomas Jefferson", "John Adams"),
        correctAnswer = "George Washington"
    ),
    Question(
        questionText = "Em que ano começou a Segunda Guerra Mundial?",
        imageResId = R.drawable.paris,
        options = listOf("1939", "1941", "1914", "1929"),
        correctAnswer = "1939"
    )
)

// Adicione mais categorias conforme necessário.

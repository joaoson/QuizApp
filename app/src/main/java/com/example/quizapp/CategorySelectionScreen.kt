package com.example.quizapp

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CategorySelectionScreen(onCategorySelected: (String, List<Question>) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Escolha uma categoria:", fontSize = 24.sp)

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { onCategorySelected("Ciência",scienceQuestions) }) {
            Text(text = "Ciências")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { onCategorySelected("História",historyQuestions) }) {
            Text(text = "História")
        }


    }
}

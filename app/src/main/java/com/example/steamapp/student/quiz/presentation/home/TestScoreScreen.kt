package com.example.steamapp.student.quiz.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.steamapp.api.domain.models.Score

@Composable
fun TestScoreScreen(
    modifier: Modifier = Modifier,
    score: Int,
    onDone:()->Unit,
    questionCount: Int
) {
    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Good try!",
            style = MaterialTheme.typography.displayMedium
        )
        Spacer(Modifier.height(10.dp))
        Text(
            text = "Score: $score / $questionCount",
            style= MaterialTheme.typography.headlineMedium
        )
        LinearProgressIndicator(
            progress = { score.toFloat()/ questionCount.toFloat() },
            modifier = modifier
                .padding(8.dp)
                .fillMaxWidth()
                .height(16.dp)
        )
        Spacer(Modifier.height(10.dp))
        Button(
            onClick = {
                onDone()
            }
        ) {
            Text("Done")
        }
    }
}
package com.example.steamapp.quiz_feature.presentation.display

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
fun ScoreScreen(
    modifier: Modifier = Modifier,
    score: Score,
    onDone:()->Unit,
    questionCount: Int
) {
    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
           text = "Scores:",
            style = MaterialTheme.typography.displayMedium
        )
        Spacer(Modifier.height(10.dp))
        Text(
            text = "Sachet: ${score.Sachet}",
            style= MaterialTheme.typography.headlineMedium
        )
       LinearProgressIndicator(
           progress = { score.Sachet/ questionCount.toFloat() },
           modifier = modifier
                   .padding(8.dp)
                   .fillMaxWidth()
                   .height(16.dp)
       )

        Spacer(Modifier.height(10.dp))
        Text(
            text = "Nidhi: ${score.Nidhi}",
            style= MaterialTheme.typography.headlineMedium
        )
        LinearProgressIndicator(
            progress = { score.Nidhi/ questionCount.toFloat() },
            modifier = modifier
                .padding(8.dp)
                .fillMaxWidth()
                .height(16.dp)
        )

        Spacer(Modifier.height(10.dp))
        Text(
            text = "Anjal: ${score.Anjal}",
            style= MaterialTheme.typography.headlineMedium
        )
        LinearProgressIndicator(
            progress = { score.Anjal/ questionCount.toFloat() },
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
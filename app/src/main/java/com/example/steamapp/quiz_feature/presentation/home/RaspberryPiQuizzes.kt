package com.example.steamapp.quiz_feature.presentation.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.steamapp.R
import com.example.steamapp.quiz_feature.domain.models.Quiz
import com.example.steamapp.quiz_feature.presentation.QuizActions
import com.example.steamapp.quiz_feature.presentation.QuizState
import com.example.steamapp.quiz_feature.presentation.home.components.QuizCard
import com.example.steamapp.ui.theme.SteamAppTheme
import java.time.Instant

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RaspberryPiQuizzesScreen(
    modifier: Modifier = Modifier,
    state: QuizState,
    onAction: (QuizActions)-> Unit,
    onConnectToPi: ()-> Unit
) {
    if(!state.connectedToPi){
        Box(
            modifier= Modifier.fillMaxSize().padding(16.dp),
            contentAlignment = Alignment.Center
        ){
            TextButton(
                onClick = onConnectToPi
            ) { 
                Text(
                    textAlign = TextAlign.Center,
                    text = "Click here to connect to the Raspberry-Pi.",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.headlineSmall,
                    textDecoration = TextDecoration.Underline
                )
            }
        }
    }
    else if(state.isLoading){
        Column(
            modifier= modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator()
            Text(
                text = "Loading data",
                color = MaterialTheme.colorScheme.secondary,
                fontSize = 16.sp
            )
        }
    }
    else if(state.remoteQuizzes.isEmpty()){
        Column(
            modifier= Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(R.drawable.emptyquizzes),
                contentDescription = "no quiz added yet",
                modifier = Modifier.size(300.dp),
                alpha = 0.8f
            )
            Text(
                text = "No Quizzes Yet.",
                color = MaterialTheme.colorScheme.secondary,
                fontSize = 16.sp
            )
        }
    } else{
        LazyColumn(
            modifier = modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(state.localQuizzes){ quiz->
                QuizCard(
                    quiz = quiz,
                    onClick = {},
                    onIconClick = {},
                    icon = R.drawable.presentation_icon,
                    onDelete = {
                        //lets see what "deleting" means for raspberry pi quizzes
                    }
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun PiQuizPreview() {
    val dummyQuizList= listOf(
        Quiz(1, "Science quiz", null, Instant.now(), 5),
        Quiz(2, "Mathematics quiz", "Simple quiz regarding mathematics", Instant.now(), 5),
        Quiz(3, "English quiz", null, Instant.now(), 5),
    )
    SteamAppTheme {
        RaspberryPiQuizzesScreen(
            state = QuizState(
                connectedToPi = false,
                remoteQuizzes = dummyQuizList
            ),
            onAction = {},
            onConnectToPi = {}
        )
    }
}
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
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

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MyQuizzesScreen(
    modifier: Modifier= Modifier,
    state: QuizState,
    onAction: (QuizActions)-> Unit,
    onNavToEditScreen: ()->Unit
) {

    if(state.isLoading){
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
    else if(state.localQuizzes.isEmpty()){
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
                    onClick = {
                        onAction(QuizActions.onLoadQuizData(it))
                        onNavToEditScreen()
                    },
                    onDelete={
                        onAction(QuizActions.onDeleteQuiz(quizId = quiz.quizId, quizName = quiz.title))
                    },
                    onIconClick = {},
                    icon = R.drawable.push_to_raspberry_pi_icon
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun MyQuizzesPreview() {
    val dummyQuizList= listOf(
        Quiz(1, "Science quiz", null, Instant.now(), 5),
        Quiz(2, "Mathematics quiz", "Simple quiz regarding mathematics", Instant.now(), 5),
        Quiz(3, "English quiz", null, Instant.now(), 5),
    )
    SteamAppTheme {
        MyQuizzesScreen(
            state = QuizState(
                localQuizzes = dummyQuizList
            ),
            onAction = {},
            onNavToEditScreen = {}
        )
    }
}
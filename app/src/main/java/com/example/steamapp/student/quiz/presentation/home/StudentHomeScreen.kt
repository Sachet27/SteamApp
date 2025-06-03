package com.example.steamapp.student.quiz.presentation.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.steamapp.quiz_feature.domain.mappers.toQuiz
import com.example.steamapp.quiz_feature.presentation.home.components.QuizCard
import com.example.steamapp.student.quiz.domain.models.DummyQuizWithQuestions
import com.example.steamapp.student.quiz.presentation.StudentQuizActions
import com.example.steamapp.student.quiz.presentation.StudentQuizState
import com.example.steamapp.ui.theme.SteamAppTheme

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun StudentHomeScreen(
    modifier: Modifier = Modifier,
    state: StudentQuizState,
    onNavToQuizTestScreen: ()->Unit,
    onAction: (StudentQuizActions)->Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .background(MaterialTheme.colorScheme.background),
        ) {
        Text(
            text = "Practice quizzes !",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        Spacer(Modifier.height(2.dp))
        Text(
            text = "Here are some practice quizzes, just for you!",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        Spacer(Modifier.height(8.dp))
        LazyColumn(
            modifier = Modifier.padding(8.dp)
        ) {
            items(state.quizzes){quiz->
                QuizCard(
                    modifier = Modifier.padding(4.dp),
                    quiz = quiz.quiz.toQuiz(),
                    icon = null,
                    onClick = {
                        onAction(StudentQuizActions.onLoadQuizForTest(it))
                        onNavToQuizTestScreen()
                        },
                    onDelete = {},
                    onIconClick = {

                    }
                )
                Spacer(Modifier.height(12.dp))
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun StudentHomePreview() {
    SteamAppTheme {
        StudentHomeScreen(
            modifier = Modifier,
            state = StudentQuizState(
                isLoading = false,
                quizzes = DummyQuizWithQuestions.quizList
            ),
            onNavToQuizTestScreen = {},
            onAction = {}
        )
    }
}
package com.example.steamapp.quiz_feature.presentation.home

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.steamapp.R
import com.example.steamapp.api.presentation.APIActions
import com.example.steamapp.api.presentation.UploadState
import com.example.steamapp.api.presentation.components.UploadDialogBox
import com.example.steamapp.quiz_feature.domain.models.Quiz
import com.example.steamapp.quiz_feature.presentation.QuizActions
import com.example.steamapp.quiz_feature.presentation.QuizState
import com.example.steamapp.quiz_feature.presentation.home.components.QuizCard
import com.example.steamapp.ui.theme.SteamAppTheme
import java.time.Instant

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MyQuizzesScreen(
    modifier: Modifier= Modifier,
    uploadState: UploadState,
    state: QuizState,
    onAPIAction: (APIActions)-> Unit,
    onAction: (QuizActions)-> Unit,
    onNavToEditScreen: ()->Unit
) {
    val context= LocalContext.current
    var showUploadDialog by remember { mutableStateOf(false) }
    var pushedQuizId by remember { mutableStateOf<Long?>(null) }
    var pushedQuizName by remember { mutableStateOf<String?>(null) }


    if(showUploadDialog){
        UploadDialogBox(
            uploadState = uploadState,
            onCancel = {
                onAPIAction(APIActions.onCancelUpload)
                val message= it?: "Upload was cancelled."
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                showUploadDialog= false
            },
            onDone = {
                showUploadDialog= false
                pushedQuizId?.let {quizId->
                    pushedQuizName?.let {quizName->
                        onAction(QuizActions.onDeleteQuiz(quizId = quizId, quizName = quizName))
                        pushedQuizId= null
                        pushedQuizName= null
                    }
                }
                onAction(QuizActions.onRefreshPiQuizzes)
            }
        )
    }
    if(state.isLoading){
        Column(
            modifier= modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface),
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
            modifier= Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
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
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
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
                    onIconClick = {
                        if(state.connectedToPi){
                            onAction(QuizActions.onSelectQuiz(quiz.quizId){ quiz->
                                quiz?.let{
                                    onAPIAction(APIActions.onPushToPi(it))
                                    showUploadDialog= true
                                    pushedQuizId= quiz.quiz.quizId
                                    pushedQuizName= quiz.quiz.title
                                }
                            }
                            )
                        } else{
                            Toast.makeText(context, "Please Connect to pi.", Toast.LENGTH_SHORT).show()
                        }
                    },
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
            onNavToEditScreen = {},
            uploadState = UploadState(),
            onAPIAction = {},
        )
    }
}
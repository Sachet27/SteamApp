package com.example.steamapp.quiz_feature.presentation.home

import android.os.Build
import android.widget.Toast
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.steamapp.R
import com.example.steamapp.api.presentation.APIActions
import com.example.steamapp.api.presentation.components.DownloadDialogBox
import com.example.steamapp.api.presentation.components.DownloadState
import com.example.steamapp.quiz_feature.domain.models.Quiz
import com.example.steamapp.quiz_feature.presentation.QuizActions
import com.example.steamapp.quiz_feature.presentation.QuizState
import com.example.steamapp.quiz_feature.presentation.home.components.RaspberryPiQuizCard
import com.example.steamapp.ui.theme.SteamAppTheme
import java.time.Instant

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RaspberryPiQuizzesScreen(
    modifier: Modifier = Modifier,
    downloadState: DownloadState,
    state: QuizState,
    onAction: (QuizActions)-> Unit,
    onAPIAction: (APIActions)->Unit,
    onConnectToPi: ()-> Unit,
    onNavToDisplayScreen: (Boolean)->Unit
) {
    var selectedQuizId by remember { mutableStateOf<Long?>(null) }
    var selectedQuizName by remember { mutableStateOf<String?>(null) }
    var showAnswer by remember { mutableStateOf(false) }
    var showDownloadDialog by remember { mutableStateOf(false) }
    val context= LocalContext.current

    if(showDownloadDialog){
        DownloadDialogBox(
            downloadState = downloadState,
            onCancel = {
                onAPIAction(APIActions.onCancelDownload)
                val message= it?: "Download was cancelled."
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                showDownloadDialog= false
            },
            onDone = {
                selectedQuizId?.let{quizId->
                    selectedQuizName?.let {quizName->
                        onAction(QuizActions.onLoadDownloadedQuiz(quizId = quizId, quizName = quizName))
                        onNavToDisplayScreen(showAnswer)
                        if(!showAnswer){
                            onAPIAction(APIActions.onPresent(quizId, quizName))
                        }
                    }
                }
                selectedQuizId= null
                selectedQuizName= null
                showAnswer= false

            }
        )
    }

    if(!state.connectedToPi){
        Box(
            modifier= Modifier
                .fillMaxSize()
                .padding(16.dp),
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
    else if(state.remoteQuizzes.isEmpty()){
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
            items(state.remoteQuizzes){ quiz->
                RaspberryPiQuizCard(
                    quiz = quiz,
                    onDelete = {
                        onAPIAction(APIActions.onDeleteFromPi(quizId = quiz.quizId, quizName= quiz.title))
                        onAction(QuizActions.onRefreshPiQuizzes)
                        onAction(QuizActions.onRefreshPiQuizzes)
                    },
                    onPresent = {
                        onAPIAction(APIActions.onDownloadFromPi(quiz = quiz))
                        showDownloadDialog= true
                        selectedQuizId= quiz.quizId
                        selectedQuizName= quiz.title
                        showAnswer= false

                    },
                    onPreview = {
                        onAPIAction(APIActions.onDownloadFromPi(quiz = quiz))
                        showDownloadDialog= true
                        selectedQuizId= quiz.quizId
                        selectedQuizName= quiz.title
                        showAnswer=true
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
        ){
            RaspberryPiQuizzesScreen(
                state = QuizState(
                    connectedToPi = true,
                    remoteQuizzes = dummyQuizList
                ),
                onAction = {},
                onConnectToPi = {

                },
                onAPIAction = {},
                modifier = Modifier,
                downloadState = DownloadState(),
                onNavToDisplayScreen = {x->}
            )
        }
    }
}
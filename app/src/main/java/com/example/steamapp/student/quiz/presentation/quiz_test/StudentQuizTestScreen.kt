package com.example.steamapp.student.quiz.presentation.quiz_test

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.common.Player
import androidx.media3.ui.PlayerView
import coil.compose.AsyncImage
import com.example.steamapp.api.presentation.APIActions
import com.example.steamapp.quiz_feature.data.local.entities.relations.QuizWithQuestions
import com.example.steamapp.quiz_feature.presentation.add_and_edit.components.QuizEditTopBar
import java.io.File

@Composable
fun StudentQuizTestScreen(
    modifier: Modifier = Modifier,
    player: Player,
    quizWithQuestions: QuizWithQuestions?,
    onBackNav: ()->Unit,
    onSetAudio: (File)->Unit,
    onNavToScoreScreen: (Int, Int)->Unit
) {
    var showAnswer by remember { mutableStateOf(false) }
    var score by remember{ mutableStateOf(0) }
    var questionIndex by remember { mutableStateOf(0) }
    val context= LocalContext.current
    var isPlaying by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        val listener= object : Player.Listener {
            override fun onIsPlayingChanged(isPlayingNow: Boolean) {
                if(isPlaying!= isPlayingNow ) {
                    isPlaying = isPlayingNow
                }
            }
        }
        player.addListener(listener)
        onDispose {
            player.removeListener(listener)
        }
    }

    var lifecycle by remember {
        mutableStateOf(Lifecycle.Event.ON_CREATE)
    }
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            lifecycle = event
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    if(quizWithQuestions== null){
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            CircularProgressIndicator()
        }
    }
    else{
        Scaffold(
            topBar = {
                QuizEditTopBar(
                    title = quizWithQuestions.quiz.title,
                    onSaveNote = {
                        player.stop()
                        onBackNav()
                    },
                    onBackNav = {
                        player.stop()
                        onBackNav()
                    }
                )
            }
        ) {padding->
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                contentAlignment = Alignment.BottomCenter
            ){
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        enabled = questionIndex>0,
                        onClick = {
                            --questionIndex
                            player.pause()
                            showAnswer= true
                        },
                    ) {
                        Text(
                            text = "Previous",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Normal
                        )
                    }

                    Button(
                        enabled = questionIndex<= quizWithQuestions.questions.size-1,
                        onClick = {
                            if(questionIndex==quizWithQuestions.questions.size-1){
                                player.stop()
                                onNavToScoreScreen(score, quizWithQuestions.questions.size)
                                showAnswer= false
                            } else{
                                questionIndex++
                                player.pause()
                                showAnswer= false
                            }
                        },
                    ) {
                        Text(
                            text = if(questionIndex == quizWithQuestions.questions.size-1) "See Scores!" else "Next",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Normal
                        )
                    }
                }

            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(top= 16.dp, end= 16.dp, start = 16.dp, bottom = 65.dp),
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = "Question ${questionIndex + 1} of ${quizWithQuestions.quiz.questionCount}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(Modifier.height(16.dp))
                quizWithQuestions.questions[questionIndex].imageRelativePath?.let{imagePath->
                    val file= File(context.filesDir, imagePath)
                    Log.d("Yeet: ","Image path: ${file.absolutePath}")

                    AsyncImage(
                        model = file.absolutePath.toUri(),
                        contentDescription = "Question Image",
                        modifier = modifier
                            .aspectRatio(4/3f)
                            .clip(RoundedCornerShape(15.dp))
                            .border(
                                BorderStroke(1.dp, MaterialTheme.colorScheme.secondary),
                                RoundedCornerShape(15.dp)
                            ),
                        contentScale = ContentScale.FillWidth
                    )
                    Spacer(Modifier.height(12.dp))
                }
                quizWithQuestions.questions[questionIndex].audioRelativePath?.let { audioPath->
                    val file= File(context.filesDir, audioPath)
                    onSetAudio(file)
                    AndroidView(
                        factory = { context ->
                            PlayerView(context).also {
                                it.player= player
                            }
                        },
                        update = {
                            when (lifecycle) {
                                Lifecycle.Event.ON_PAUSE -> {
                                    it.onPause()
                                    it.player?.pause()
                                }
                                Lifecycle.Event.ON_RESUME -> {
                                    it.onResume()

                                }
                                else -> Unit
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(16 / 9f)
                    )
                    Spacer(Modifier.height(12.dp))
                }
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    item{
                        Text(
                            text = quizWithQuestions.questions[questionIndex].title,
                            style= MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp),
                        )
                        Spacer(Modifier.height(12.dp))
                    }
                    itemsIndexed(quizWithQuestions.questions[questionIndex].options){index, option->
                        val optionNumber= when(index){
                            0-> "A"
                            1->"B"
                            2->"C"
                            3->"D"
                            else -> ""
                        }
                        Row(
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(4.dp)
                                .clip(RoundedCornerShape(15.dp))
                                .border(
                                    border = BorderStroke(1.dp,
                                        if(!showAnswer){
                                            MaterialTheme.colorScheme.secondary
                                        } else{
                                            if(index == quizWithQuestions.questions[questionIndex].correctOptionIndex){
                                                Color.Green
                                            } else MaterialTheme.colorScheme.error
                                        }),
                                         shape = RoundedCornerShape(15.dp)
                                )
                                .clickable {
                                    showAnswer= true
                                    if(index== quizWithQuestions.questions[questionIndex].correctOptionIndex) score++
                                },
                            verticalAlignment = Alignment.CenterVertically,

                            ){
                            Text(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 16.dp),
                                text = "$optionNumber) $option",
                                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 14.sp),
                                color= if(!showAnswer){
                                    MaterialTheme.colorScheme.onSurface
                                } else{
                                    if(index == quizWithQuestions.questions[questionIndex].correctOptionIndex)
                                        Color.Green else MaterialTheme.colorScheme.error
                                }
                            )
                        }
                        Spacer(Modifier.height(12.dp))
                    }
                }
            }
        }

    }

}



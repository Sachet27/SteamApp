package com.example.steamapp.quiz_feature.presentation.display


import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
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
import com.example.steamapp.quiz_feature.data.local.entities.QuestionEntity
import com.example.steamapp.quiz_feature.data.local.entities.QuizEntity
import com.example.steamapp.quiz_feature.data.local.entities.relations.QuizWithQuestions
import com.example.steamapp.quiz_feature.presentation.add_and_edit.components.QuizEditTopBar
import com.example.steamapp.ui.theme.SteamAppTheme
import java.io.File
import java.time.Instant

@Composable
fun DisplayScreen(
    modifier: Modifier = Modifier,
    player:Player,
    quizWithQuestions: QuizWithQuestions,
    onBackNav: ()->Unit,
    showAnswer: Boolean,
    onSetAudio: (File)->Unit,
    onAPIActions: (APIActions)->Unit
) {
    var questionIndex by remember { mutableStateOf(0) }
    val context= LocalContext.current
    var isPlaying by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        val listener= object : Player.Listener {
            override fun onIsPlayingChanged(isPlayingNow: Boolean) {
                if(isPlaying!= isPlayingNow ) {
                    isPlaying = isPlayingNow
                    if (!showAnswer) {
                        if (isPlayingNow) {
                            onAPIActions(APIActions.onPlayAudio)
                        } else {
                            onAPIActions(APIActions.onPauseAudio)
                        }
                    }
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

    Scaffold(
        topBar = {
            QuizEditTopBar(
                title = quizWithQuestions.quiz.title,
                onSaveNote = {
                    player.stop()
                    if(!showAnswer) {
                        onAPIActions(APIActions.onExit)
                    }
                    onBackNav()
                },
                onBackNav = {
                    player.stop()
                    if(!showAnswer) {
                        onAPIActions(APIActions.onExit)
                    }
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
                        if(!showAnswer) {
                            onAPIActions(APIActions.onPrevious)
                        }
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
                            if(!showAnswer){
                                onAPIActions(APIActions.onFinish)
                            }
                            onBackNav()
                        } else{
                            questionIndex++
                            player.pause()
                            if(!showAnswer){
                                onAPIActions(APIActions.onNext)
                            }
                        }
                    },
                ) {
                    Text(
                        text = if(questionIndex == quizWithQuestions.questions.size-1) "Finish" else "Next",
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
                    .padding(16.dp),
               verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "${questionIndex+1}) ${quizWithQuestions.questions[questionIndex].title}",
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold
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
                   )
                   Spacer(Modifier.height(16.dp))
               }
               quizWithQuestions.questions[questionIndex].audioRelativePath?.let { audioPath->
                   val file= File(context.filesDir, audioPath)
                   onSetAudio(file)
                   AndroidView(
                       factory = { context ->
                           PlayerView(context).also {
                               it.player = player
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


               }
               LazyColumn(
                   modifier = Modifier
                       .fillMaxWidth()
                       .padding(8.dp)
               ) {
                   itemsIndexed(quizWithQuestions.questions[questionIndex].options){index, option->
                       val optionNumber= when(index){
                           0-> "A"
                           1->"B"
                           2->"C"
                           3->"D"
                           else -> ""
                       }
                       Row(
                           modifier = modifier.fillMaxWidth(),
                           verticalAlignment = Alignment.CenterVertically,
                           horizontalArrangement = Arrangement.SpaceBetween
                       ){
                           Text(
                               text = "$optionNumber) $option",
                               style = MaterialTheme.typography.titleMedium,
                               color= if(showAnswer && index == quizWithQuestions.questions[questionIndex].correctOptionIndex) Color.Green else MaterialTheme.colorScheme.onSurface
                           )
                       }
                       Spacer(Modifier.height(12.dp))
                   }
               }
           }
    }
}



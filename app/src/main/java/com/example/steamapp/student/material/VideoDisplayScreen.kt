package com.example.steamapp.student.material

import android.app.Activity
import android.content.pm.ActivityInfo
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
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
import com.example.steamapp.quiz_feature.presentation.add_and_edit.components.QuizEditTopBar
import com.example.steamapp.student.quiz.presentation.StudentQuizState
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoDisplayScreen(
    modifier: Modifier= Modifier,
    syncWithPi: Boolean= false,
    player: Player,
    state: StudentQuizState,
    onSetAudio: (File)->Unit,
    onAPIActions: (APIActions)->Unit,
    onBackNav: ()->Unit
) {
    var isPlaying by remember { mutableStateOf(false) }
    val context= LocalContext.current

    DisposableEffect(Unit) {
        val listener= object : Player.Listener {
            override fun onIsPlayingChanged(isPlayingNow: Boolean) {
                if(isPlaying!= isPlayingNow ) {
                    isPlaying = isPlayingNow
                    if (!syncWithPi) {
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
            TopAppBar(
                title = {
                    Text(
                        text = state.selectedMaterial?.name ?: "Video",
                        fontWeight = FontWeight.W500,
                        style = MaterialTheme.typography.headlineLarge,
                        modifier = Modifier.padding(16.dp),
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                navigationIcon = {
                    IconButton(
                        onClick = {
                            if(syncWithPi){
                                onAPIActions(APIActions.onExit)
                            }
                            player.stop()
                            onBackNav()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "Back Nav icon",
                            tint= MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                )
            )
        }
    ) {padding->
        if(state.selectedMaterial== null) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ){
                CircularProgressIndicator()
            }
        } else{
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            verticalArrangement = Arrangement.Center
        ) {
                val file = File(context.filesDir, state.selectedMaterial.pdfUri)
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
                        .fillMaxSize()
//                        .aspectRatio(16 / 9f)
                )
                Spacer(Modifier.height(12.dp))
            }
            }
        }
}
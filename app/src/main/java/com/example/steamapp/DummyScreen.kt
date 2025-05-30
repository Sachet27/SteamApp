package com.example.steamapp

import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import coil.compose.AsyncImage
import com.example.steamapp.api.domain.models.Score
import com.example.steamapp.api.presentation.APIActions
import com.example.steamapp.quiz_feature.data.local.entities.QuestionEntity
import com.example.steamapp.quiz_feature.data.local.entities.QuizEntity
import com.example.steamapp.quiz_feature.data.local.entities.relations.QuizWithQuestions
import com.example.steamapp.quiz_feature.presentation.QuizActions
import com.example.steamapp.quiz_feature.presentation.add_and_edit.MediaState
import com.example.steamapp.quiz_feature.presentation.add_and_edit.QuizFormState
import com.example.steamapp.quiz_feature.presentation.add_and_edit.components.QuizEditTopBar
import com.example.steamapp.ui.theme.SteamAppTheme
import java.io.File
import java.time.Instant

@Composable
fun EnhancedAddEditScreen(
    modifier: Modifier = Modifier,
    state: QuizFormState,
    mediaState: MediaState,
    onAction: (QuizActions) -> Unit,
    onBackNav: () -> Unit,
    onStoreMedia: (contentUri: Uri, quizName: String, questionId: Long, quizId: Long) -> Unit
) {
    val imageAudioPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { contentUri ->
            contentUri?.let {
                onStoreMedia(contentUri, state.quizTitle, 0, 0)
            }
        }
    )

    Scaffold(
        topBar = { /* Add an improved top bar here */ },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                // Title Input Section
                Text(
                    text = "Quiz Title",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                OutlinedTextField(
                    value = state.title,
                    onValueChange = { onAction(QuizActions.onChangeTitle(it)) },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Enter title here") }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Options Input Section
                Text(
                    text = "Options",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        OutlinedTextField(value = state.optionA, onValueChange = { onAction(QuizActions.onChangeOptionA(it)) }, placeholder = { Text("Option A") })
                        Spacer(Modifier.height(8.dp))
                        OutlinedTextField(value = state.optionC, onValueChange = { onAction(QuizActions.onChangeOptionC(it)) }, placeholder = { Text("Option C") })
                    }
                    Column {
                        OutlinedTextField(value = state.optionB, onValueChange = { onAction(QuizActions.onChangeOptionB(it)) }, placeholder = { Text("Option B") })
                        Spacer(Modifier.height(8.dp))
                        OutlinedTextField(value = state.optionD, onValueChange = { onAction(QuizActions.onChangeOptionD(it)) }, placeholder = { Text("Option D") })
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Upload Section
                Text(
                    text = "Media Upload",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = { imageAudioPicker.launch("image/*") },
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp)
                    ) {
                        Text("Upload Image")
                    }
                    Button(
                        onClick = { imageAudioPicker.launch("audio/*") },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Upload Audio")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Navigation Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = { /* Navigate to Previous */ },
                        enabled = true // Replace with actual logic
                    ) {
                        Text("Previous")
                    }
                    Button(
                        onClick = { /* Navigate to Next */ },
                        enabled = true // Replace with actual logic
                    ) {
                        Text("Next")
                    }
                }
            }
        }
    )
}

@Preview
@Composable
private fun AddEDitPreview() {
    SteamAppTheme {
        EnhancedAddEditScreen(
            state = QuizFormState(),
            mediaState = MediaState(),
            onAction = {},
            onBackNav = {},
            onStoreMedia = { _, _, _, _->

            }
        ) 
    }
}
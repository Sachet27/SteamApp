package com.example.steamapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.steamapp.quiz_feature.domain.models.Quiz
import com.example.steamapp.quiz_feature.presentation.QuizState
import com.example.steamapp.quiz_feature.presentation.components.AppBottomBar
import com.example.steamapp.quiz_feature.presentation.components.AppTopBar
import com.example.steamapp.quiz_feature.presentation.components.BottomNavItems
import com.example.steamapp.quiz_feature.presentation.components.BottomNavigationList
import com.example.steamapp.quiz_feature.presentation.home.HomeScreen
import com.example.steamapp.ui.theme.SteamAppTheme
import java.time.Instant

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SteamAppTheme {
                val dummyQuizList= listOf(
                    Quiz(1, "Science quiz", null, Instant.now(), 5),
                    Quiz(2, "Mathematics quiz", "Simple quiz regarding mathematics".repeat(4), Instant.now(), 5),
                    Quiz(3, "English quiz", null, Instant.now(), 5),
                )
                    HomeScreen(
                        state = QuizState(
                            connectedToPi = true,
                            localQuizzes = dummyQuizList,
                            remoteQuizzes = dummyQuizList
                        )
                    ) { }
                }
            }
        }
    }



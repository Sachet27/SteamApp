package com.example.steamapp.core.navigation

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.steamapp.api.presentation.APIActions
import com.example.steamapp.api.presentation.APIEvents
import com.example.steamapp.api.presentation.APIViewModel
import com.example.steamapp.api.presentation.AskAIScreen
import com.example.steamapp.core.presentation.ObserveAsEvents
import com.example.steamapp.core.presentation.toString
import com.example.steamapp.quiz_feature.data.local.entities.QuizEntity
import com.example.steamapp.quiz_feature.data.local.entities.relations.QuizWithQuestions
import com.example.steamapp.quiz_feature.presentation.QuizActions
import com.example.steamapp.quiz_feature.presentation.QuizViewModel
import com.example.steamapp.quiz_feature.presentation.add_and_edit.AddEditScreen
import com.example.steamapp.quiz_feature.presentation.audio_playback.MediaViewModel
import com.example.steamapp.quiz_feature.presentation.components.BottomNavItems
import com.example.steamapp.quiz_feature.presentation.display.DisplayScreen
import com.example.steamapp.quiz_feature.presentation.home.HomeScreen
import kotlinx.coroutines.flow.merge
import org.koin.androidx.compose.koinViewModel
import java.time.Instant

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavHost() {
        val apiViewModel: APIViewModel = koinViewModel()
        val quizViewModel: QuizViewModel = koinViewModel()
        val mediaViewModel: MediaViewModel = koinViewModel()

        val uploadState by apiViewModel.uploadState.collectAsStateWithLifecycle()
        val downloadState by apiViewModel.downloadState.collectAsStateWithLifecycle()
        val mediaState by quizViewModel.mediaState.collectAsStateWithLifecycle()
        val quizState by quizViewModel.quizState.collectAsStateWithLifecycle()
        val quizFormState by quizViewModel.quizFormState.collectAsStateWithLifecycle()
        val quizWithQuestions by quizViewModel.selectedQuiz.collectAsStateWithLifecycle()
        val aiQuestionState by apiViewModel.aiQuestionState.collectAsStateWithLifecycle()

        val events = merge(quizViewModel.quizEvents, apiViewModel.events)
        val context = LocalContext.current
        ObserveAsEvents(events = events) { event ->
            when (event) {
                is APIEvents.Error -> {
                    Toast.makeText(context, event.error.toString(context), Toast.LENGTH_LONG).show()
                }
            }
        }

        val navController = rememberNavController()

        NavHost(navController = navController, startDestination = NavRoutes.HomeRoute) {
            composable<NavRoutes.HomeRoute> {
                HomeScreen(
                    state = quizState,
                    onAction = quizViewModel::onAction,
                    onNavToEditQuizScreen = {
                        navController.navigate(NavRoutes.AddEditRoute)
                    },
                    uploadState = uploadState,
                    onAPIAction = apiViewModel::onAction,
                    onConnectToPi = {
                        //wifi connection stuff halnu parxa
                        quizViewModel.getAllRemoteQuizzes()
                    },
                    downloadState = downloadState,
                    onNavToDisplayScreen = { showAnswer ->
                        navController.navigate(NavRoutes.DisplayRoute(showAnswer))
                    },
                    onBottomItemClick = {
                        when (it) {
                            BottomNavItems.QUIZ -> {
                                navController.navigate(NavRoutes.HomeRoute) {
                                    popUpTo(NavRoutes.HomeRoute) { inclusive = true }
                                }
                            }

                            BottomNavItems.MATERIAL -> {}
                            BottomNavItems.CREATE -> {
                                quizViewModel.onAction(QuizActions.onLoadQuizData(null))
                                navController.navigate(NavRoutes.AddEditRoute)
                            }
                        }
                    },
                    onNavToAIScreen = { navController.navigate(NavRoutes.AskAIRoute) }
                )
            }
            composable<NavRoutes.AddEditRoute> {
                AddEditScreen(
                    state = quizFormState,
                    onAction = quizViewModel::onAction,
                    onBackNav = {
                        navController.popBackStack()
                    },
                    onStoreMedia = { contentUri, quizName, questionId, quizId ->
                        quizViewModel.saveMediaInInternalStorage(
                            contentUri,
                            quizName,
                            questionId,
                            quizId
                        )
                    },
                    mediaState = mediaState
                )
            }

            //crash happens when this is encountered
            composable<NavRoutes.DisplayRoute> {
                val args = it.toRoute<NavRoutes.DisplayRoute>()
                val showAnswer = args.showAnswer
                val quiz = quizWithQuestions
                DisplayScreen(
                    player = mediaViewModel.player,
                    quizWithQuestions = quiz?: QuizWithQuestions(
                        quiz = QuizEntity(0L,"", null, Instant.now(), 0),
                        questions = emptyList()
                    ),
                    onBackNav = {
                        navController.popBackStack()
                    },
                    showAnswer = showAnswer,
                    onSetAudio = {
                        val uri = it.absolutePath.toUri()
                        mediaViewModel.setAudioUri(uri)
                    }
                )
            }

            composable<NavRoutes.AskAIRoute> {
                AskAIScreen(
                    state = aiQuestionState,
                    onAPIActions = apiViewModel::onAction,
                    onBackNav = {
                        navController.popBackStack()
                        apiViewModel.onAction(APIActions.onClearAIQuestionState)
                    }
                )
            }
        }
}
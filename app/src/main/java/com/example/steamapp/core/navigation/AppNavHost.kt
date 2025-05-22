package com.example.steamapp.core.navigation

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.steamapp.api.presentation.APIEvents
import com.example.steamapp.api.presentation.APIViewModel
import com.example.steamapp.core.presentation.ObserveAsEvents
import com.example.steamapp.core.presentation.toString
import com.example.steamapp.quiz_feature.presentation.QuizViewModel
import com.example.steamapp.quiz_feature.presentation.add_and_edit.AddEditScreen
import com.example.steamapp.quiz_feature.presentation.home.HomeScreen
import org.koin.androidx.compose.koinViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavHost() {
    val apiViewModel:APIViewModel = koinViewModel()
    val quizViewModel: QuizViewModel= koinViewModel()

    val uploadState by apiViewModel.uploadState.collectAsStateWithLifecycle()
    val mediaState by quizViewModel.mediaState.collectAsStateWithLifecycle()
    val quizState by quizViewModel.quizState.collectAsStateWithLifecycle()
    val quizFormState by quizViewModel.quizFormState.collectAsStateWithLifecycle()

    val context= LocalContext.current
    ObserveAsEvents(events = apiViewModel.events){event->
        when(event){
            is APIEvents.Error-> {
                Toast.makeText(context, event.error.toString(context), Toast.LENGTH_LONG).show()
            }
        }
    }

    val navController= rememberNavController()
    NavHost(navController = navController, startDestination = NavRoutes.HomeRoute){
        composable<NavRoutes.HomeRoute> {
            HomeScreen(
                state = quizState,
                onAction = quizViewModel::onAction,
                onNavToEditQuizScreen = {
                    navController.navigate(NavRoutes.AddEditRoute)
                },
                uploadState = uploadState,
                onAPIAction = apiViewModel::onAction
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
                    quizViewModel.saveMediaInInternalStorage(contentUri, quizName, questionId, quizId)
                },
                mediaState = mediaState
            )
        }
    }
}
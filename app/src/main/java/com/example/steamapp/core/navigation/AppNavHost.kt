package com.example.steamapp.core.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.steamapp.quiz_feature.presentation.QuizActions
import com.example.steamapp.quiz_feature.presentation.QuizViewModel
import com.example.steamapp.quiz_feature.presentation.add_and_edit.AddEditScreen
import com.example.steamapp.quiz_feature.presentation.home.HomeScreen
import org.koin.androidx.compose.koinViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavHost() {
    val quizViewModel: QuizViewModel= koinViewModel()
    val quizState by quizViewModel.quizState.collectAsStateWithLifecycle()
    val quizFormState by quizViewModel.quizFormState.collectAsStateWithLifecycle()
    val navController= rememberNavController()
    NavHost(navController = navController, startDestination = NavRoutes.HomeRoute){
        composable<NavRoutes.HomeRoute> {
            HomeScreen(
                state = quizState,
                onAction = quizViewModel::onAction,
                onNavToEditQuizScreen = {
                    navController.navigate(NavRoutes.AddEditRoute)
                }
            )
        }
        composable<NavRoutes.AddEditRoute> {
            AddEditScreen(
                state = quizFormState,
                onAction = quizViewModel::onAction,
                onBackNav = {
                    navController.popBackStack()
                }
            )
        }
    }
}
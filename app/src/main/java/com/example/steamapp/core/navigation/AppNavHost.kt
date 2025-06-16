package com.example.steamapp.core.navigation

import android.app.Activity.RESULT_OK
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.steamapp.api.domain.models.Score
import com.example.steamapp.api.presentation.APIActions
import com.example.steamapp.api.presentation.APIEvents
import com.example.steamapp.api.presentation.APIViewModel
import com.example.steamapp.api.presentation.AskAIScreen
import com.example.steamapp.auth.presentation.AuthActions
import com.example.steamapp.auth.presentation.AuthViewModel
import com.example.steamapp.auth.presentation.login.LoginScreen
import com.example.steamapp.core.presentation.AddQuizOrMaterialDialog
import com.example.steamapp.core.presentation.CustomScaffold
import com.example.steamapp.core.presentation.ObserveAsEvents
import com.example.steamapp.core.presentation.student.StudentListScreen
import com.example.steamapp.core.presentation.toString
import com.example.steamapp.material_feature.domain.models.StudyMaterial
import com.example.steamapp.material_feature.presentation.MaterialActions
import com.example.steamapp.material_feature.presentation.home.MaterialScreen
import com.example.steamapp.material_feature.presentation.MaterialViewModel
import com.example.steamapp.material_feature.presentation.components.MaterialInfoDialog
import com.example.steamapp.material_feature.presentation.display.DisplayPdfScreen
import com.example.steamapp.quiz_feature.data.local.entities.QuizEntity
import com.example.steamapp.quiz_feature.data.local.entities.relations.QuizWithQuestions
import com.example.steamapp.quiz_feature.presentation.QuizActions
import com.example.steamapp.quiz_feature.presentation.QuizViewModel
import com.example.steamapp.quiz_feature.presentation.add_and_edit.AddEditScreen
import com.example.steamapp.quiz_feature.presentation.audio_playback.MediaViewModel
import com.example.steamapp.quiz_feature.presentation.components.BottomNavItems
import com.example.steamapp.quiz_feature.presentation.display.DisplayScreen
import com.example.steamapp.quiz_feature.presentation.display.ScoreScreen
import com.example.steamapp.quiz_feature.presentation.home.HomeScreen
import com.example.steamapp.student.StudentProfileScreen
import com.example.steamapp.student.material.StudentMaterialScreen
import com.example.steamapp.student.material.VideoDisplayScreen
import com.example.steamapp.student.quiz.components.CustomStudentScaffold
import com.example.steamapp.student.quiz.presentation.StudentQuizActions
import com.example.steamapp.student.quiz.presentation.home.StudentHomeScreen
import com.example.steamapp.student.quiz.presentation.StudentViewModel
import com.example.steamapp.student.quiz.presentation.home.TestScoreScreen
import com.example.steamapp.student.quiz.presentation.quiz_test.StudentQuizTestScreen
import kotlinx.coroutines.flow.merge
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.getKoin
import java.time.Instant

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavHost() {


    val koin = getKoin()
    val studentScope = remember { koin.createScope<SubGraph.StudentRoute>() }

    val apiViewModel: APIViewModel = koinViewModel()
    val quizViewModel: QuizViewModel = koinViewModel()
    val mediaViewModel: MediaViewModel = koinViewModel()
    val authViewModel: AuthViewModel = koinViewModel()
    val materialViewModel: MaterialViewModel = koinViewModel()

    val infoState by materialViewModel.infoState.collectAsStateWithLifecycle()
    val uploadState by apiViewModel.uploadState.collectAsStateWithLifecycle()
    val downloadState by apiViewModel.downloadState.collectAsStateWithLifecycle()
    val materialUploadState by apiViewModel.materialUploadState.collectAsStateWithLifecycle()
    val materialDownloadState by apiViewModel.materialDownloadState.collectAsStateWithLifecycle()
    val mediaState by quizViewModel.mediaState.collectAsStateWithLifecycle()
    val quizState by quizViewModel.quizState.collectAsStateWithLifecycle()
    val quizFormState by quizViewModel.quizFormState.collectAsStateWithLifecycle()
    val quizWithQuestions by quizViewModel.selectedQuiz.collectAsStateWithLifecycle()
    val aiQuestionState by apiViewModel.aiQuestionState.collectAsStateWithLifecycle()
    val authState by authViewModel.authState.collectAsStateWithLifecycle()
    val materialState by materialViewModel.materialState.collectAsStateWithLifecycle()
    val studentListState by materialViewModel.studentListState.collectAsStateWithLifecycle()


    // 0 -> Upload existing pdf, 1-> create new pdf
    val selectMaterial by materialViewModel.selectedMaterial.collectAsStateWithLifecycle()
    var actionCode by remember { mutableStateOf(0) }
    var selectedBottomNavBarItem by remember { mutableStateOf(BottomNavItems.QUIZ) }
    var showMaterialUploadDialog by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    val userId by authViewModel.userId.collectAsStateWithLifecycle(null)
    val role by authViewModel.userRole.collectAsStateWithLifecycle(null)
    val scores by quizViewModel.scores.collectAsStateWithLifecycle()

    val events = merge(
        quizViewModel.quizEvents,
        apiViewModel.events,
        authViewModel.events,
        materialViewModel.events
    )
    val context = LocalContext.current
    ObserveAsEvents(events = events) { event ->
        when (event) {
            is APIEvents.Error -> {
                Toast.makeText(context, event.error.toString(context), Toast.LENGTH_LONG).show()
            }
        }
    }

    var startDestination by remember {
        mutableStateOf(
            if (role == null) {
                SubGraph.AuthRoute
            } else {
                if (role == "STUDENT")
                    SubGraph.StudentRoute
                else
                    SubGraph.QuizRoute
            }
        )
    }


    val navController = rememberNavController()

    val scannerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = {
            if (it.resultCode == RESULT_OK) {
                materialViewModel.handleScanResult(
                    it.data,
                    context = context,
                )
            }
        }
    )

    val uploadPdfLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = {
            it?.let { uri ->
                materialViewModel.onAction(MaterialActions.onInsertMaterial(uri = uri, context))
            }
        }
    )

    if (showMaterialUploadDialog) {
        MaterialInfoDialog(
            infoState = infoState,
            onAction = materialViewModel::onAction,
            onDismiss = {
                showMaterialUploadDialog = false
                if (actionCode == 0) { //existing pdf

                    uploadPdfLauncher.launch("application/pdf")

                } else if (actionCode == 1) { //create new pdf

                    materialViewModel.getStartScanIntent(
                        context = context,
                        onSuccess = {
                            scannerLauncher.launch(IntentSenderRequest.Builder(it).build())
                        },
                        onError = {
                            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                        }
                    )
                }
            }
        )
    }

    if (showDialog) {
        AddQuizOrMaterialDialog(
            onDismiss = {
                showDialog = false
            },
            onCreateQuiz = {
                quizViewModel.onAction(QuizActions.onLoadQuizData(null))
                navController.navigate(NavRoutes.AddEditRoute)
                showDialog = false
            },
            onUploadPdf = {
                showDialog = false
                actionCode = 0
                showMaterialUploadDialog = true
            },
            onCreatePdf = {
                showDialog = false
                actionCode = 1
                showMaterialUploadDialog = true
            },
            onActions = materialViewModel::onAction
        )
    }


    NavHost(navController = navController, startDestination = startDestination) {
        navigation<SubGraph.AuthRoute>(startDestination = NavRoutes.LoginRoute) {
            composable<NavRoutes.LoginRoute> {
                LoginScreen(
                    onAction = authViewModel::onAction,
                    state = authState,
                    onSignIn = {
                        if (role == "STUDENT") {
                            navController.navigate(SubGraph.StudentRoute)
                        } else if (role == "TEACHER") {
                            navController.navigate(SubGraph.QuizRoute)
                        }
                    },
                )
            }
        }

        navigation<SubGraph.QuizRoute>(startDestination = NavRoutes.HomeRoute) {
            composable<NavRoutes.HomeRoute> {
                CustomScaffold(
                    onBottomItemClick = {
                        when (it) {
                            BottomNavItems.QUIZ -> {
                                navController.navigate(NavRoutes.HomeRoute) {
                                    popUpTo(NavRoutes.HomeRoute) { inclusive = true }
                                }
                            }

                            BottomNavItems.MATERIAL -> {
                                navController.navigate(SubGraph.MaterialRoute)
                            }

                            BottomNavItems.CREATE -> {
                                showDialog = true
                            }
                        }

                    },
                    onNavToAIScreen = { navController.navigate(NavRoutes.AskAIRoute) },
                    userId = userId,
                    onSignOut = {
                        navController.navigate(SubGraph.AuthRoute) {
                            popUpTo(0) { inclusive = true }
                        }
                        authViewModel.onAction(AuthActions.OnSignOut)
                    },
                    onSelectItem = {
                        selectedBottomNavBarItem = it
                    },
                    selectedItem = selectedBottomNavBarItem,
                    onStudentListClick = {
                        materialViewModel.onAction(MaterialActions.onLoadStudentsList)
                        navController.navigate(
                            NavRoutes.StudentListRoute
                        )
                    }
                ) {
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
                        }
                    )
                }
            }
            composable<NavRoutes.AddEditRoute> {
                AddEditScreen(
                    state = quizFormState,
                    onAction = quizViewModel::onAction,
                    onBackNav = {
                        selectedBottomNavBarItem = BottomNavItems.QUIZ
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
            composable<NavRoutes.DisplayRoute> {
                val args = it.toRoute<NavRoutes.DisplayRoute>()
                val showAnswer = args.showAnswer
                val quiz = quizWithQuestions
                DisplayScreen(
                    player = mediaViewModel.player,
                    quizWithQuestions = quiz ?: QuizWithQuestions(
                        quiz = QuizEntity(0L, "", null, Instant.now(), 0),
                        questions = emptyList()
                    ),
                    onBackNav = {
                        navController.popBackStack()
                        selectedBottomNavBarItem = BottomNavItems.QUIZ
                    },
                    showAnswer = showAnswer,
                    onSetAudio = {
                        val uri = it.absolutePath.toUri()
                        mediaViewModel.setAudioUri(uri)
                    },
                    onAPIActions = apiViewModel::onAction,
                    onFetchScores = { quizViewModel.onAction(QuizActions.onGetScores) },
                    onNavToScoreScreen = {
                        navController.navigate(
                            NavRoutes.ScoreRoute(
                                quiz?.quiz?.questionCount ?: 1
                            )
                        )
                    },
                )
            }
            composable<NavRoutes.AskAIRoute> {
                AskAIScreen(
                    state = aiQuestionState,
                    onAPIActions = apiViewModel::onAction,
                    onBackNav = {
                        selectedBottomNavBarItem = BottomNavItems.QUIZ
                        navController.popBackStack()
                        apiViewModel.onAction(APIActions.onClearAIQuestionState)
                    },
                    userId = userId ?: "Guest"
                )
            }

            composable<NavRoutes.ScoreRoute> {
                val args = it.toRoute<NavRoutes.ScoreRoute>()
                val questionCount = args.questionCount
                ScoreScreen(
                    score = scores ?: Score(0, 0, 0),
                    onDone = {
                        quizViewModel.onAction(QuizActions.onClearScores)
                        navController.navigate(NavRoutes.HomeRoute) {
                            popUpTo(NavRoutes.HomeRoute) { inclusive = true }
                        }
                    },
                    questionCount = questionCount
                )
            }
        }

        navigation<SubGraph.MaterialRoute>(startDestination = NavRoutes.MaterialHomeRoute) {

            composable<NavRoutes.MaterialHomeRoute> {
                CustomScaffold(
                    onBottomItemClick = {
                        when (it) {
                            BottomNavItems.QUIZ -> {
                                navController.navigate(NavRoutes.HomeRoute) {
                                    popUpTo(NavRoutes.HomeRoute) { inclusive = true }
                                }
                            }

                            BottomNavItems.MATERIAL -> {}
                            BottomNavItems.CREATE -> {
                                showDialog = true
                            }
                        }

                    },
                    onNavToAIScreen = { navController.navigate(NavRoutes.AskAIRoute) },
                    userId = userId,
                    onSignOut = {
                        navController.navigate(SubGraph.AuthRoute) {
                            popUpTo(0) { inclusive = true }
                        }
                        authViewModel.onAction(AuthActions.OnSignOut)
                    },
                    onSelectItem = {
                        selectedBottomNavBarItem = it
                    },
                    selectedItem = selectedBottomNavBarItem,
                    onStudentListClick = {
                        materialViewModel.onAction(MaterialActions.onLoadStudentsList)
                        navController.navigate(NavRoutes.StudentListRoute)
                    }
                ) {
                    MaterialScreen(
                        state = materialState,
                        onMaterialAction = materialViewModel::onAction,
                        onAPIActions = apiViewModel::onAction,
                        uploadState = materialUploadState,
                        downloadState = materialDownloadState,
                        onNavToDisplayPdfScreen = { notSyncWithPi ->
                            navController.navigate(NavRoutes.PdfDisplayRoute(notSyncWithPi))
                        }
                    )
                }
            }

            composable<NavRoutes.PdfDisplayRoute> {
                val args = it.toRoute<NavRoutes.PdfDisplayRoute>()
                val syncWithPi = !args.notSyncWithPi
                DisplayPdfScreen(
                    modifier = Modifier,
                    syncWithPi = syncWithPi,
                    material = selectMaterial ?: StudyMaterial(
                        id = 0L,
                        name = "No material",
                        description = null,
                        pdfUri = "",
                        pages = 0
                    ),
                    onAPIActions = apiViewModel::onAction,
                    onBackNav = {
                        navController.popBackStack()
                    },
                    onMaterialActions = materialViewModel::onAction
                )
            }

            composable<NavRoutes.StudentListRoute> {
                StudentListScreen(
                    state = studentListState,
                    onBackNav = { navController.popBackStack() },
                    onMaterialActions = materialViewModel::onAction,
                    onNavToStudentDetailScreen = {
                        navController.navigate(NavRoutes.StudentDetailRoute)
                    }
                )
            }

            composable<NavRoutes.StudentDetailRoute> {
                StudentProfileScreen(
                    studentDetail = studentListState.selectedStudentReport,
                    onBackNav = {
                        materialViewModel.onAction(MaterialActions.onClearSelectedStudentReport)
                        navController.popBackStack()
                    }
                )
            }

        }

        navigation<SubGraph.StudentRoute>(startDestination = NavRoutes.StudentHomeRoute) {
            val studentViewModel: StudentViewModel = studentScope.get()

            composable<NavRoutes.StudentHomeRoute> {
                val state by studentViewModel.quizState.collectAsStateWithLifecycle()

                CustomStudentScaffold(
                    selectedItem = selectedBottomNavBarItem,
                    onBottomItemClick = {
                        when (it) {
                            BottomNavItems.QUIZ -> {
                            }

                            BottomNavItems.MATERIAL -> {
                                navController.navigate(NavRoutes.StudentMaterialRoute)
                            }

                            BottomNavItems.CREATE -> {}
                        }
                    },
                    onNavToAIScreen = {
                        navController.navigate(NavRoutes.StudentAskAIRoute)
                    },
                    userId = userId,
                    onSignOut = {
                        navController.navigate(SubGraph.AuthRoute) {
                            popUpTo(0) { inclusive = true }
                        }
                        authViewModel.onAction(AuthActions.OnSignOut)
                    },
                    onSelectItem = { selectedBottomNavBarItem = it },
                    onProfileClick = {
                        userId?.let {
                            studentViewModel.onAction(StudentQuizActions.onLoadStudentReport(it))
                            navController.navigate(NavRoutes.StudentProfileRoute)
                        }?: Toast.makeText(context, "Report hasn't been made yet", Toast.LENGTH_SHORT).show()
                    }
                ) {
                    StudentHomeScreen(
                        state = state,
                        onNavToQuizTestScreen = {
                            navController.navigate(NavRoutes.StudentQuizTestRoute)
                        },
                        onAction = studentViewModel::onAction
                    )
                }
            }

            composable<NavRoutes.StudentMaterialRoute> {
                val state by studentViewModel.quizState.collectAsStateWithLifecycle()

                CustomStudentScaffold(
                    selectedItem = selectedBottomNavBarItem,
                    onBottomItemClick = {
                        when (it) {
                            BottomNavItems.QUIZ -> {
                                navController.navigate(NavRoutes.StudentHomeRoute) {
                                    popUpTo(NavRoutes.StudentHomeRoute) { inclusive = true }
                                }
                            }

                            BottomNavItems.MATERIAL -> {
                            }

                            BottomNavItems.CREATE -> {}
                        }
                    },
                    onNavToAIScreen = {
                        navController.navigate(NavRoutes.StudentAskAIRoute)
                    },
                    userId = userId,
                    onSignOut = {
                        studentViewModel.onAction(StudentQuizActions.onClearStudentReport)
                        navController.navigate(SubGraph.AuthRoute) {
                            popUpTo(0) { inclusive = true }
                        }
                        authViewModel.onAction(AuthActions.OnSignOut)
                    },
                    onSelectItem = { selectedBottomNavBarItem = it },
                    onProfileClick = {
                        userId?.let {
                            studentViewModel.onAction(StudentQuizActions.onLoadStudentReport(it))
                            navController.navigate(NavRoutes.StudentProfileRoute)
                        }?: Toast.makeText(context, "Report hasn't been made yet", Toast.LENGTH_SHORT).show()
                    }
                ) {
                    StudentMaterialScreen(
                        state = state,
                        onAction = studentViewModel::onAction,
                        onNavToDisplayPdfScreen = {
                            navController.navigate(
                                NavRoutes.DummyPdfDisplayRoute
                            )
                        },
                        onNavToVideoScreen = {
                            navController.navigate(
                                NavRoutes.DummyVideoDisplayRoute
                            )
                        }
                    )
                }
            }

            composable<NavRoutes.StudentQuizTestRoute> {
                val state by studentViewModel.quizState.collectAsStateWithLifecycle()
                StudentQuizTestScreen(
                    player = mediaViewModel.player,
                    quizWithQuestions = state.selectedQuiz,
                    onBackNav = {
                        navController.popBackStack()
                    },
                    onSetAudio = {
                        val uri = it.absolutePath.toUri()
                        mediaViewModel.setAudioUri(uri)
                    },
                    onNavToScoreScreen = { score, total ->
                        navController.navigate(NavRoutes.StudentTestQuizScoreRoute(score, total))
                    }
                )
            }

            composable<NavRoutes.StudentTestQuizScoreRoute> {
                Log.d("Yeet", "Hello")
                val args = it.toRoute<NavRoutes.StudentTestQuizScoreRoute>()
                val score = args.score
                val total = args.total
                TestScoreScreen(
                    score = score,
                    onDone = {
                        studentViewModel.onAction(StudentQuizActions.onClearSelectedQuiz)
                        navController.navigate(NavRoutes.StudentHomeRoute) {
                            popUpTo(NavRoutes.StudentHomeRoute) { inclusive = true }
                        }
                    },
                    questionCount = total
                )
            }

            composable<NavRoutes.DummyPdfDisplayRoute> {
                val state by studentViewModel.quizState.collectAsStateWithLifecycle()
                DisplayPdfScreen(
                    syncWithPi = false,
                    material = state.selectedMaterial ?: StudyMaterial(
                        id = 0L,
                        name = "No material",
                        description = null,
                        pdfUri = "",
                        pages = 0
                    ),
                    onAPIActions = apiViewModel::onAction,
                    onBackNav = {
                        navController.popBackStack()
                        studentViewModel.onAction(StudentQuizActions.onClearSelectedMaterial)
                    },
                    onMaterialActions = materialViewModel::onAction
                )
            }

            composable<NavRoutes.DummyVideoDisplayRoute> {
                val state by studentViewModel.quizState.collectAsStateWithLifecycle()
                VideoDisplayScreen(
                    syncWithPi = false,
                    player = mediaViewModel.player,
                    state = state,
                    onSetAudio = {
                        val uri = it.absolutePath.toUri()
                        mediaViewModel.setAudioUri(uri)
                    },
                    onAPIActions = apiViewModel::onAction,
                    modifier = Modifier,
                    onBackNav = {navController.popBackStack()}
                )
            }

            composable<NavRoutes.StudentAskAIRoute> {
                AskAIScreen(
                    state = aiQuestionState,
                    onAPIActions = apiViewModel::onAction,
                    onBackNav = {
                        selectedBottomNavBarItem = BottomNavItems.QUIZ
                        navController.popBackStack()
                        apiViewModel.onAction(APIActions.onClearAIQuestionState)
                    },
                    userId = userId ?: "Guest"
                )
            }

            composable<NavRoutes.StudentProfileRoute> {
                val studentDetail by studentViewModel.studentDetails.collectAsStateWithLifecycle()
                LaunchedEffect(studentDetail) {
                    Log.d("Yeet", studentDetail.toString())
                }
                ObserveAsEvents(events = studentViewModel.events) { event ->
                    when (event) {
                        is APIEvents.Error -> {
                            Toast.makeText(context, event.error.toString(context), Toast.LENGTH_LONG).show()
                        }
                    }
                }
                StudentProfileScreen(
                    modifier= Modifier,
                    studentDetail= studentDetail,
                    onBackNav = {
                        studentViewModel.onAction(StudentQuizActions.onClearStudentReport)
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}







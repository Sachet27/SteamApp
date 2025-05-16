package com.example.steamapp.quiz_feature.presentation.add_and_edit

import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.steamapp.core.util.replace
import com.example.steamapp.quiz_feature.data.local.entities.QuestionEntity
import com.example.steamapp.quiz_feature.data.local.entities.QuizEntity
import com.example.steamapp.quiz_feature.data.local.entities.relations.QuizWithQuestions
import com.example.steamapp.quiz_feature.domain.mappers.toQuestion
import com.example.steamapp.quiz_feature.presentation.QuizActions
import com.example.steamapp.quiz_feature.presentation.add_and_edit.components.AddEditQuizDialog
import com.example.steamapp.quiz_feature.presentation.add_and_edit.components.QuizEditTopBar
import com.example.steamapp.ui.theme.SteamAppTheme
import java.time.Instant

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddEditScreen(
    modifier: Modifier= Modifier,
    state: QuizFormState,
    onAction: (QuizActions)-> Unit,
    onBackNav: ()-> Unit,
    onStoreMedia: (contentUri: Uri, quizName: String, questionId: Long)->Unit
) {
    var questionIndex by remember { mutableStateOf(0) }
    var currentQuiz by remember { mutableStateOf(
        state.quizWithQuestions
            ?:QuizWithQuestions(
                quiz = QuizEntity(
                    quizId = 0L,
                    title = "Untitled quiz",
                    description = null,
                    lastUpdatedAt = Instant.now(),
                    questionCount = 0
                ),
                questions = emptyList()
            )
    ) }
    var showDialog by remember { mutableStateOf(true) }

    val imageAudioPicker= rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = {contentUri->
            contentUri?.let {
                onStoreMedia(contentUri, currentQuiz.quiz.title, (questionIndex+1).toLong())
            }
        }
    )

    Scaffold(
        topBar = {
            QuizEditTopBar(
                title = state.quizTitle,
                onSaveNote = {
                    if(state.quizWithQuestions==null){
                        if(currentQuiz.questions.isEmpty() || questionIndex >= currentQuiz.questions.size) {
                            val newQuestion = QuestionEntity(
                                id = 0L,
                                title = state.title,
                                options = listOf(
                                    state.optionA,
                                    state.optionB,
                                    state.optionC,
                                    state.optionD
                                ),
                                correctOptionIndex = state.correctOptionIndex,
                                imageRelativePath = state.imageRelativePath,
                                audioRelativePath = state.audioRelativePath,
                                quizId = currentQuiz.quiz.quizId
                            )
                            currentQuiz = currentQuiz.copy(
                                questions = currentQuiz.questions + newQuestion
                            )
                        } else{
                            val replacedQuestionList= currentQuiz.questions.replace(questionIndex, state)
                            currentQuiz= currentQuiz.copy(
                                questions = replacedQuestionList
                            )
                        }
                        onAction(QuizActions.onInsertQuiz(
                            currentQuiz.copy(
                                quiz = currentQuiz.quiz.copy(
                                    questionCount = currentQuiz.questions.size
                                )
                            )
                        ))
                    } else{
                        val replacedQuestionList= currentQuiz.questions.replace(questionIndex, state)
                        currentQuiz= currentQuiz.copy(
                            questions = replacedQuestionList
                        )
                        onAction(QuizActions.onUpdateQuiz(
                            currentQuiz.copy(
                                quiz = currentQuiz.quiz.copy(
                                   questionCount = currentQuiz.questions.size
                                )
                            )
                        ))
                    }
                    onAction(QuizActions.onClearData)
                    onBackNav()
                },
                onBackNav = {
                    onAction(QuizActions.onClearData)
                    onBackNav()
                }
            )
        }
    ) {padding->
        if(showDialog){
            AddEditQuizDialog(
                state = state,
                onAction = onAction,
                onDismiss = {
                    currentQuiz= currentQuiz.copy(
                        quiz = currentQuiz.quiz.copy(
                            title = state.quizTitle,
                            description = state.quizDescription
                        )
                    )
                    showDialog=false
                },
            )
        }
        if(state.isLoading){
            Column(
                modifier= modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
                Text(
                    text = "Processing",
                    color = MaterialTheme.colorScheme.secondary,
                    fontSize = 16.sp
                )
            }
        }
        else{
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Question",
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(12.dp))
                Text(
                    text = "Title:",
                    style = MaterialTheme.typography.headlineSmall,
                    fontSize = 18.sp
                )
                Spacer(Modifier.height(4.dp))
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = state.title,
                    onValueChange = {onAction(QuizActions.onChangeTitle(it))},
                    placeholder = { Text("Enter the title", color = MaterialTheme.colorScheme.secondary) }
                )
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier= Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Column(
                        modifier= Modifier
                            .weight(0.5f)
                            .padding(8.dp)
                    ) {
                        Text(
                            text = "Option A:",
                            style = MaterialTheme.typography.headlineSmall,
                            fontSize = 18.sp
                        )
                        Spacer(Modifier.height(4.dp))
                        OutlinedTextField(
                            value = state.optionA,
                            onValueChange = {onAction(QuizActions.onChangeOptionA(it)) },
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = "Option C:",
                            style = MaterialTheme.typography.headlineSmall,
                            fontSize = 18.sp
                        )
                        Spacer(Modifier.height(4.dp))
                        OutlinedTextField(
                            value = state.optionC,
                            onValueChange = {onAction(QuizActions.onChangeOptionC(it))  },
                        )
                    }

                    Column(
                        modifier= Modifier
                            .weight(0.5f)
                            .padding(8.dp)
                    ) {
                        Text(
                            text = "Option B:",
                            style = MaterialTheme.typography.headlineSmall,
                            fontSize = 18.sp
                        )
                        Spacer(Modifier.height(4.dp))
                        OutlinedTextField(
                            value = state.optionB,
                            onValueChange = {onAction(QuizActions.onChangeOptionB(it))  },
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = "Option D:",
                            style = MaterialTheme.typography.headlineSmall,
                            fontSize = 18.sp
                        )
                        Spacer(Modifier.height(4.dp))
                        OutlinedTextField(
                            value = state.optionD,
                            onValueChange = {onAction(QuizActions.onChangeOptionD(it))},
                        )
                    }
                }
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ){
                    (0..3).forEach { index->
                        val option= when(index){
                            0-> "A"
                            1-> "B"
                            2-> "C"
                            3-> "D"
                            else -> ""
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            RadioButton(
                                selected = index == state.correctOptionIndex,
                                onClick = {
                                    onAction(QuizActions.onChangeAnswerIndex(index))
                                }
                            )
                            Text(
                                text = option,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier= Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier
                            .weight(0.5f)
                            .padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Image:",
                            style = MaterialTheme.typography.headlineSmall,
                            fontSize = 18.sp
                        )
                        Spacer(Modifier.height(4.dp))
                        Button(
                            onClick = {
                                imageAudioPicker.launch("image/*")
                            }
                        ) {
                            Text("Upload")
                        }
                    }
                    Column(
                        modifier = Modifier
                            .weight(0.5f)
                            .padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Audio:",
                            style = MaterialTheme.typography.headlineSmall,
                            fontSize = 18.sp
                        )
                        Spacer(Modifier.height(4.dp))
                        Button(
                            onClick = {
                                imageAudioPicker.launch("audio/*")
                            }
                        ) {
                            Text("Upload")
                        }
                    }
                }
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        enabled = questionIndex>0,
                        onClick = {
                            if(questionIndex== currentQuiz.questions.size){
                                val newQuestion= QuestionEntity(
                                    id = 0L,
                                    title = state.title,
                                    options = listOf(
                                        state.optionA,
                                        state.optionB,
                                        state.optionC,
                                        state.optionD
                                    ),
                                    correctOptionIndex = state.correctOptionIndex,
                                    imageRelativePath = state.imageRelativePath,
                                    audioRelativePath = state.audioRelativePath,
                                    quizId = currentQuiz.quiz.quizId
                                )
                                currentQuiz= currentQuiz.copy(
                                    questions = currentQuiz.questions+ newQuestion
                                )
                            } else{
                                val replacedQuestionList= currentQuiz.questions.replace(questionIndex, state)
                                currentQuiz= currentQuiz.copy(
                                    questions = replacedQuestionList
                                )
                            }
                                --questionIndex
                                onAction(QuizActions.onChangeQuestion(currentQuiz.questions[questionIndex].toQuestion()))
                            },
                    ) {
                        Text(
                            text = "Previous",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Normal
                        )
                    }

                    Button(
                        enabled = if(state.quizWithQuestions==null) true else questionIndex< currentQuiz.questions.size-1,
                        onClick = {
                                if(currentQuiz.questions.isEmpty() || questionIndex >= currentQuiz.questions.size){
                                    val newQuestion= QuestionEntity(
                                        id = 0L,
                                        title = state.title,
                                        options = listOf(
                                            state.optionA,
                                            state.optionB,
                                            state.optionC,
                                            state.optionD
                                        ),
                                        correctOptionIndex = state.correctOptionIndex,
                                        imageRelativePath = state.imageRelativePath,
                                        audioRelativePath = state.audioRelativePath,
                                        quizId = currentQuiz.quiz.quizId
                                    )
                                    currentQuiz= currentQuiz.copy(
                                        questions = currentQuiz.questions + newQuestion
                                    )
                                    questionIndex++
                                    onAction(QuizActions.onClearFormData)
                                     } else{
                                    val replacedQuestionList= currentQuiz.questions.replace(questionIndex, state)
                                    currentQuiz= currentQuiz.copy(
                                        questions = replacedQuestionList
                                    )
                                    questionIndex++
                                    if(questionIndex== currentQuiz.questions.size){
                                        onAction(QuizActions.onClearFormData)
                                    } else{
                                        onAction(QuizActions.onChangeQuestion(currentQuiz.questions[questionIndex].toQuestion()))
                                    }
                                     }
                        },
                    ) {
                        Text(
                            text = "Next",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Normal
                        )
                    }

                }
            }

        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun AddEditPreview() {
    SteamAppTheme {
        AddEditScreen(
            state = QuizFormState(
                quizWithQuestions = QuizWithQuestions(
                    quiz = QuizEntity(
                        quizId = 2,
                        title = "My quiz",
                        description = "",
                        lastUpdatedAt = Instant.now(),
                        questionCount = 0,
                    ),
                    questions = listOf()
                )
            ),
            onAction = {},
            onBackNav = {},
            onStoreMedia = {contentUri, quizName, questionId ->

            }
        )
    }
}


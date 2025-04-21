package com.example.steamapp.quiz_feature.presentation.add_and_edit

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Button
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.steamapp.quiz_feature.data.local.entities.QuizEntity
import com.example.steamapp.quiz_feature.data.local.entities.relations.QuizWithQuestions
import com.example.steamapp.quiz_feature.presentation.QuizActions
import com.example.steamapp.quiz_feature.presentation.add_and_edit.components.QuizEditTopBar
import com.example.steamapp.ui.theme.SteamAppTheme
import java.time.Instant

@Composable
fun AddEditScreen(
    modifier: Modifier= Modifier,
    state: QuizFormState,
    onAction: (QuizActions)-> Unit,
    onBackNav: ()-> Unit
) {
    val quiz= state.quizWithQuestions
    Log.d("Error", quiz?.quiz.toString())
    Scaffold(
        topBar = {
            QuizEditTopBar(
                title = quiz?.let {quiz.quiz.title} ?: "Untitled Quiz",
                onSaveNote = {
                    onBackNav()
                    onAction(QuizActions.onClearData)
                },
            )
        }
    ) {padding->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)
        ) {
            Text(
                text = "Question ${state.questionCount+1}",
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
                    modifier= Modifier.weight(0.5f).padding(8.dp)
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
                    modifier= Modifier.weight(0.5f).padding(8.dp)
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
                    modifier = Modifier.weight(0.5f).padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Image:",
                        style = MaterialTheme.typography.headlineSmall,
                        fontSize = 18.sp
                    )
                    Spacer(Modifier.height(4.dp))
                    Button(
                        onClick = {}
                    ) {
                        Text("Upload")
                    }
                }
                Column(
                    modifier = Modifier.weight(0.5f).padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Audio:",
                        style = MaterialTheme.typography.headlineSmall,
                        fontSize = 18.sp
                    )
                    Spacer(Modifier.height(4.dp))
                    Button(
                        onClick = {}
                    ) {
                        Text("Upload")
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                        //need to do some implementation here
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
            onBackNav = {}
        )
    }
}
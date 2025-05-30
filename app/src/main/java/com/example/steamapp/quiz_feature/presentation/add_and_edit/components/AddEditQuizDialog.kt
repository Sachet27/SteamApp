package com.example.steamapp.quiz_feature.presentation.add_and_edit.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.steamapp.quiz_feature.data.local.entities.QuizEntity
import com.example.steamapp.quiz_feature.data.local.entities.relations.QuizWithQuestions
import com.example.steamapp.quiz_feature.presentation.QuizActions
import com.example.steamapp.quiz_feature.presentation.add_and_edit.QuizFormState
import com.example.steamapp.ui.theme.SteamAppTheme
import java.time.Instant

@Composable
fun AddEditQuizDialog(
    state: QuizFormState,
    onAction: (QuizActions)->Unit,
    onDismiss: ()->Unit
) {
    AlertDialog(
        title = {
            Text(
                text = "Set Title and Description",
                style = MaterialTheme.typography.titleLarge,
                fontSize = 20.sp
            )
        },
        onDismissRequest = {
            onDismiss()
        },
        confirmButton = {
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    onDismiss()
                },
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Save")
            }
        },
        text = {
            Column {
                Text(
                    text = "Title:",
                    style = MaterialTheme.typography.headlineSmall,
                )
                Spacer(Modifier.height(4.dp))
                OutlinedTextField(
                    value = state.quizTitle,
                    onValueChange = { onAction(QuizActions.onChangeQuizTitle(it)) },
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Description:",
                    style = MaterialTheme.typography.headlineSmall,
                )
                Spacer(Modifier.height(4.dp))
                OutlinedTextField(
                    value = state.quizDescription ?: "",
                    onValueChange = { onAction(QuizActions.onChangeQuizDescription(it)) },
                    placeholder = {
                        Text(
                            text = "No description yet",
                            style = MaterialTheme.typography.labelMedium
                        )
                    },

                )
            }
        }
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun DialogPreview() {
    SteamAppTheme {
        AddEditQuizDialog(
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
        ) { }
    }
}
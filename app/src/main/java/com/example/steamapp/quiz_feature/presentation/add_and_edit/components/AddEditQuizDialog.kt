package com.example.steamapp.quiz_feature.presentation.add_and_edit.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.steamapp.quiz_feature.presentation.QuizActions
import com.example.steamapp.quiz_feature.presentation.add_and_edit.QuizFormState

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
            TextButton(onClick = {
                onDismiss()
            }) {
                Text("Save")
            }
        },
        text = {
            Column {
                Text(
                    text = "Title:",
                    style = MaterialTheme.typography.headlineSmall,
                    fontSize = 18.sp
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
                    fontSize = 18.sp
                )
                Spacer(Modifier.height(4.dp))
                OutlinedTextField(
                    value = state.quizDescription ?: "",
                    onValueChange = { onAction(QuizActions.onChangeQuizDescription(it)) },
                )
            }
        }
    )
}
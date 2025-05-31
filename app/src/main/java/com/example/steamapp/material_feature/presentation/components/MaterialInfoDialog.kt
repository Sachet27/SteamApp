package com.example.steamapp.material_feature.presentation.components

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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.steamapp.material_feature.presentation.MaterialActions
import com.example.steamapp.material_feature.presentation.MaterialInfoState
import com.example.steamapp.quiz_feature.data.local.entities.QuizEntity
import com.example.steamapp.quiz_feature.data.local.entities.relations.QuizWithQuestions
import com.example.steamapp.quiz_feature.presentation.QuizActions
import com.example.steamapp.quiz_feature.presentation.add_and_edit.QuizFormState
import com.example.steamapp.ui.theme.SteamAppTheme
import java.time.Instant

@Composable
fun MaterialInfoDialog(
    infoState: MaterialInfoState,
    onAction: (MaterialActions)->Unit,
    onDismiss: ()->Unit
) {
//0 -> Upload existing pdf
//1-> Create new pdf
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
                Text("Upload")
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
                    value = infoState.title,
                    onValueChange = { onAction(MaterialActions.onChangeTitle(it)) },
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Description:",
                    style = MaterialTheme.typography.headlineSmall,
                )
                Spacer(Modifier.height(4.dp))
                OutlinedTextField(
                    value = infoState.description ?: "",
                    onValueChange = { onAction(MaterialActions.onChangeDescription(it)) },
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
        MaterialInfoDialog(
            infoState = MaterialInfoState("Chapter1", ""),
            onAction = {},
        ) { }
    }
}
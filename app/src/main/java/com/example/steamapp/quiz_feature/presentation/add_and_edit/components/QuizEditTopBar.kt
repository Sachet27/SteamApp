package com.example.steamapp.quiz_feature.presentation.add_and_edit.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.steamapp.ui.theme.SteamAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizEditTopBar(
    title: String,
    onSaveNote: ()->Unit,
    onBackNav: ()->Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                fontSize = 25.sp,
                modifier = Modifier.padding(16.dp),
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackNav
            ) { 
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = null,
                )
            }
        },
        actions = {
            TextButton(
                onClick = {
                    onSaveNote()
                }
            ) {
                Text(
                    text = "Done",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(8.dp)
                )

            }
        }
    )
}

@Preview
@Composable
private fun QuizEditPreview() {
    SteamAppTheme {
        QuizEditTopBar(
            title = "Add Quiz",
            onSaveNote = {},
            onBackNav = {}
        )
    }
}
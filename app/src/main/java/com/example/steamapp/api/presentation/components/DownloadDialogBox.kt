package com.example.steamapp.api.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.steamapp.api.presentation.UploadState
import kotlin.math.roundToInt

@Composable
fun DownloadDialogBox(
    modifier: Modifier = Modifier,
    downloadState: DownloadState,
    onCancel: (String?)->Unit,
    onDone: ()-> Unit
) {
    AlertDialog(
        title = {
            Text(
                text= "Downloading files..",
                style = MaterialTheme.typography.titleLarge,
                fontSize = 20.sp
            )
        },
        dismissButton = {
            OutlinedButton(
                enabled = downloadState.isUploading,
                onClick = { onCancel(downloadState.error) }
            ) {
                Text("Cancel")
            }
        },
        onDismissRequest = {},
        confirmButton = {
            Button(
                enabled= downloadState.isUploadComplete,
                onClick = onDone
            ) {
                Text("Proceed")
            }
        },
        text = {
            Column(
                modifier = modifier.padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val animatedProgress= animateFloatAsState(
                    targetValue = downloadState.progress,
                    animationSpec = tween(100),
                    label= "File download progress bar"
                )
                if(downloadState.error==null && downloadState.isUploading) {
                    LinearProgressIndicator(
                        progress = { downloadState.progress },
                        modifier = modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                            .height(16.dp)
                    )
                    Text(
                        text = "Progress: ${(downloadState.progress * 100).roundToInt()}%",
                        style = MaterialTheme.typography.bodyMedium
                    )
                } else{
                    Text(
                        text = "Successfully downloaded from Pi!",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    )
}
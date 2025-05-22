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
fun UploadDialogBox(
    modifier: Modifier = Modifier,
    uploadState: UploadState,
    onCancel: (String?)->Unit,
    onDone: ()-> Unit
) {
     AlertDialog(
         title = {
             Text(
                 text= "Uploading file..",
                 style = MaterialTheme.typography.titleLarge,
                 fontSize = 20.sp
             )
         },
         dismissButton = {
             OutlinedButton(
                 enabled = uploadState.isUploading,
                 onClick = { onCancel(uploadState.error) }
             ) {
                 Text("Cancel")
             }
         },
         onDismissRequest = {},
         confirmButton = {
             Button(
                 enabled= uploadState.isUploadComplete,
                 onClick = onDone
             ) {
                 Text("Done")
             }
         },
         text = {
             Column(
                 modifier = modifier.padding(16.dp),
                 verticalArrangement = Arrangement.Center,
                 horizontalAlignment = Alignment.CenterHorizontally
             ) {
                 val animatedProgress= animateFloatAsState(
                     targetValue = uploadState.progress,
                     animationSpec = tween(100),
                     label= "File upload progress bar"
                 )
                 if(uploadState.error==null && uploadState.isUploading) {
                     LinearProgressIndicator(
                         progress = { uploadState.progress },
                         modifier = modifier
                             .padding(8.dp)
                             .fillMaxWidth()
                             .height(16.dp)
                     )
                     Text(
                         text = "Progress: ${(uploadState.progress * 100).roundToInt()}%",
                         style = MaterialTheme.typography.bodyMedium
                     )
                 } else{
                     Text(
                         text = "Successfully uploaded to Pi!",
                         style = MaterialTheme.typography.titleMedium
                     )
                 }
             }
         }
     )
}
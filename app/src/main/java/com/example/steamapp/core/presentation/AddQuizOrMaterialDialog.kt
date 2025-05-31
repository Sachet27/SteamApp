package com.example.steamapp.core.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RadialGradient
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.steamapp.R
import com.example.steamapp.material_feature.presentation.MaterialActions
import com.example.steamapp.quiz_feature.presentation.QuizActions
import com.example.steamapp.ui.theme.SteamAppTheme

@Composable
fun AddQuizOrMaterialDialog(
    modifier: Modifier = Modifier,
    onDismiss:()->Unit,
    onCreateQuiz: ()->Unit,
    onUploadPdf: ()->Unit,
    onCreatePdf: ()->Unit,
    onActions: (MaterialActions)->Unit
) {
    AlertDialog(
        title = {
            Text(
                text = "Select an option",
                style = MaterialTheme.typography.titleLarge,
                fontSize = 20.sp
            )
        },
        onDismissRequest = {
            onDismiss()
        },
        confirmButton = {
            OutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    onDismiss()
                },
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Cancel")
            }
        },
        text = {
            Column(
                modifier= Modifier
                    .fillMaxWidth()
                    .size(200.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .border(BorderStroke(0.5.dp, MaterialTheme.colorScheme.secondary), shape = RoundedCornerShape(15.dp))
                    .background(MaterialTheme.colorScheme.surfaceBright),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().weight(0.4f).padding(8.dp).clickable { onCreateQuiz() },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Add a quiz",
                            style= MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Light
                        )
                        Spacer(Modifier.height(6.dp))
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.quiz_icon_outlined),
                            contentDescription = null,
                        )
                    }
                }
                HorizontalDivider(color = MaterialTheme.colorScheme.onSurface)
                Row(
                    modifier = Modifier.fillMaxWidth().weight(0.4f),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(
                        modifier = Modifier.weight(0.4f).clickable { onUploadPdf() },
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Text(
                            textAlign = TextAlign.Center,
                            text = "Upload a material",
                            lineHeight = 18.sp,
                            style= MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Light
                        )
                        Spacer(Modifier.height(6.dp))
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.upload_icon),
                            contentDescription = null,
                        )
                    }

                    VerticalDivider(color = MaterialTheme.colorScheme.onSurface)

                    Column(
                        modifier= Modifier.weight(0.4f).clickable { onCreatePdf() },
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            textAlign = TextAlign.Center,
                            text = "Create pdf",
                            style= MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Light
                        )
                        Spacer(Modifier.height(6.dp))
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.camera_icon),
                            contentDescription = null,
                        )
                    }
                }
            }
        }
    )
}

@Preview
@Composable
private fun AddQuizOrMaterialDialogPreview() {
    SteamAppTheme{
        AddQuizOrMaterialDialog(
            modifier = Modifier,
            onDismiss = {},
            onActions = {},
            onCreateQuiz = {},
            onUploadPdf = {},
            onCreatePdf = {}
        )
    }
}
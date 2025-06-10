package com.example.steamapp.student

import android.widget.Space
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.steamapp.R
import com.example.steamapp.api.presentation.APIActions
import com.example.steamapp.ui.theme.SteamAppTheme
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentProfileScreen(
    modifier: Modifier = Modifier,
    studentDetail: StudentDetail?,
    onBackNav: ()->Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                title = {
                    Text(
                        text = "Student Report",
                        fontWeight = FontWeight.W500,
                        style = MaterialTheme.typography.headlineLarge,
                        modifier = Modifier.padding(16.dp),
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onBackNav()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "Back Nav icon",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            )
        }
    ) {innerPadding->
        if(studentDetail== null){
            Column(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                CircularProgressIndicator()
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Fetching your reports. Please wait.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        } else{
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
            ) {
                Text(
                    text = "Profile Overview",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(4.dp))
                Column(
                    modifier = modifier.fillMaxWidth().padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(R.drawable.default_pfp),
                        contentDescription = null,
                        modifier= modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .border(
                                border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.onSurface),
                                shape = CircleShape
                            )
                    )
                    Spacer(Modifier.height(10.dp))
                    Text(
                        text = studentDetail.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(24.dp))
                }

                Text(
                    text = "Student Statistics",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Overall Rating:",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "You are in the ${studentDetail.classification} percentile range!",
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(Modifier.height(8.dp))
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ){
                    LoadingBar(
                        canvasSize = 190.dp,
                        indicatorValue = studentDetail.rating.toInt()
                    )
                }

                Text(
                    text = "Subject-wise Scores:",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )

                val dataPoints= mapOf(
                    Pair(studentDetail.subject_ratios.Maths ,"Maths"),
                    Pair(studentDetail.subject_ratios.Nepali,"Nepali"),
                    Pair(studentDetail.subject_ratios.Social ,"Social"),
                    Pair(studentDetail.subject_ratios.English ,"English"),
                    Pair(studentDetail.subject_ratios.Science ,"Science"),
                )
                BarChart(
                    data = dataPoints,
                    max_value = 100
                )

                Spacer(Modifier.height(12.dp))

                Text(
                    text = "Analysis",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Strengths:",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(4.dp))
                LazyRow{
                    items(studentDetail.strengths){subject->
                        AssistChip(
                            onClick = {},
                            label = {
                                Text(
                                    text = subject.replaceFirstChar { it.uppercase() },
                                    style = MaterialTheme.typography.labelLarge
                                )
                            },
                            modifier = Modifier.padding(end = 12.dp),
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = MaterialTheme.colorScheme.background,
                                labelColor = MaterialTheme.colorScheme.onSurface
                            )
                        )
                    }
                }
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Weaknesses:",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(4.dp))
                LazyRow{
                    items(studentDetail.weaknesses){subject->
                        AssistChip(
                            onClick = {},
                            label = {
                                Text(
                                    text = subject.replaceFirstChar { it.uppercase() },
                                    style = MaterialTheme.typography.labelLarge
                                )
                            },
                            modifier = Modifier.padding(end = 12.dp),
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = MaterialTheme.colorScheme.background,
                                labelColor = MaterialTheme.colorScheme.onSurface
                            )
                        )
                    }
                }

            }
        }

    }
}



@Preview
@Composable
private fun StudentProfilePreview() {
    SteamAppTheme {
        StudentProfileScreen(
            studentDetail = StudentDetail(
                name = "Sachet Kayastha",
                rating = 72.0f,
                classification = "70-80",
                strengths = listOf("Maths", "Science"),
                weaknesses = listOf("English", "Social"),
                subject_ratios = SubjectScores(
                    Maths = 1.0f,
                    Science = 0.95f,
                    Nepali = 0.7f,
                    English = 0.62f,
                    Social = 0.6f
                ),
            ),
            onBackNav = {}
        )
    }
}
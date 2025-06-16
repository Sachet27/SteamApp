package com.example.steamapp.core.presentation.student

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.steamapp.R
import com.example.steamapp.material_feature.presentation.MaterialActions
import com.example.steamapp.ui.theme.SteamAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentListScreen(
    modifier: Modifier = Modifier,
    state: StudentListState,
    onBackNav: ()->Unit,
    onMaterialActions: (MaterialActions)->Unit,
    onNavToStudentDetailScreen: ()->Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Student List",
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
                            tint= MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                )
            )
        }
    ){innerPadding->
        if(state.isLoading){
            Box(
                modifier= Modifier.fillMaxSize().padding(innerPadding),
                contentAlignment = Alignment.Center
            ){
                CircularProgressIndicator()
            }
        } else if(state.studentList.isEmpty()){
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(R.drawable.no_user_found),
                    contentDescription = "no students added yet",
                    modifier = Modifier.size(300.dp),
                    alpha = 0.8f
                )
                Text(
                    text= "No students added :(",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
        else{
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
            ) {
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Text(
                        text = "Students:",
                        style = MaterialTheme.typography.headlineLarge
                    )

                    Text(
                        text = "${state.studentList.size} students",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                HorizontalDivider(Modifier.padding(vertical = 8.dp))
                Spacer(Modifier.height(8.dp))
                LazyColumn {
                    items(state.studentList){ student->
                        Row(
                            modifier = modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ){
                                Image(
                                    painter = painterResource(R.drawable.default_pfp),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(30.dp)
                                        .clip(CircleShape),
                                )
                                Text(
                                    text = student,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                            IconButton(
                                onClick = {
                                    onMaterialActions(MaterialActions.onLoadStudentReport(student))
                                    onNavToStudentDetailScreen()
                                }
                            ) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(R.drawable.analytics_24px),
                                    contentDescription = null
                                )
                            }
                        }
                        HorizontalDivider(Modifier.padding(vertical = 6.dp))
                    }
                }
            }
        }
    }
}


@Preview
@Composable
private fun StudentListPreview() {
    SteamAppTheme {
        StudentListScreen(
            state = StudentListState(
                isLoading = false,
//                studentList = emptyList(),
                studentList = listOf(
                    "Anjal Rajchal",
                    "Nidhi Pradhan",
                    "Sachet Kayastha",
                    "Sumi Bhedi",
                    "Pushpa Lal Shrestha",
                    "Nischal Shrestha"
                ),
                selectedStudentReport = null
            ),
            onBackNav = {},
            onNavToStudentDetailScreen = {},
            onMaterialActions = {}
        )
    }
}
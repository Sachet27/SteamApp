package com.example.steamapp.quiz_feature.presentation.home

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.steamapp.R
import com.example.steamapp.api.presentation.APIActions
import com.example.steamapp.api.presentation.UploadState
import com.example.steamapp.api.presentation.components.DownloadState
import com.example.steamapp.quiz_feature.data.local.entities.relations.QuizWithQuestions
import com.example.steamapp.quiz_feature.domain.models.Quiz
import com.example.steamapp.quiz_feature.presentation.QuizActions
import com.example.steamapp.quiz_feature.presentation.QuizState
import com.example.steamapp.quiz_feature.presentation.components.AppBottomBar
import com.example.steamapp.quiz_feature.presentation.components.AppTopBar
import com.example.steamapp.quiz_feature.presentation.components.BottomNavItems
import com.example.steamapp.quiz_feature.presentation.components.BottomNavigationList
import com.example.steamapp.ui.theme.SteamAppTheme
import java.time.Instant

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    state: QuizState,
    uploadState: UploadState,
    downloadState: DownloadState,
    onAction: (QuizActions)-> Unit,
    onAPIAction: (APIActions)-> Unit,
    onNavToEditQuizScreen: ()->Unit,
    onConnectToPi: ()->Unit,
    onNavToDisplayScreen: (Boolean)->Unit,
) {

    var selectedIndex by remember{ mutableStateOf(0) }
    val pagerState= rememberPagerState { TabItem.tabItemsList.size }

    LaunchedEffect(selectedIndex) {
        pagerState.animateScrollToPage(selectedIndex)
    }
    LaunchedEffect(pagerState.currentPage, pagerState.isScrollInProgress) {
        if(!pagerState.isScrollInProgress){
            selectedIndex= pagerState.currentPage
        }
    }

        Column(
            modifier= Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ){
//            Spacer(Modifier.height(8.dp))
            TabRow(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .border(BorderStroke(0.25.dp, MaterialTheme.colorScheme.secondary), RoundedCornerShape(15.dp))
                    .shadow(elevation = 10.dp)
                ,
                containerColor = Color(0xFFFEFEFE),
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                selectedTabIndex = selectedIndex
            ){
                TabItem.tabItemsList.forEachIndexed { index, tabItem ->
                    Tab(
                        modifier = Modifier.clip(RoundedCornerShape(15.dp)),
                        selected = index==selectedIndex,
                        onClick = {
                            selectedIndex= index
                        },
                        text = {
                            Text(
                                color = if(index== selectedIndex) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                                text = tabItem.title,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        },
                        icon = {
                            Icon(
                                imageVector = ImageVector.vectorResource(
                                    if (index == selectedIndex) tabItem.selectedIcon else tabItem.unselectedIcon
                                ),
                                contentDescription = null,
                                modifier = Modifier.size(30.dp),
                                tint = Color.Unspecified
                            )
                        },
                    )
                }
            }
            HorizontalPager(
                state = pagerState,
                modifier= Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) { index->
                when(index){
                    0-> MyQuizzesScreen(
                        modifier = Modifier.fillMaxSize(),
                        state = state,
                        onAction = onAction,
                        onNavToEditScreen = {
                            onNavToEditQuizScreen()
                        },
                        uploadState = uploadState,
                        onAPIAction = onAPIAction,
                    )
                    1->{
                        RaspberryPiQuizzesScreen(
                            modifier = Modifier.fillMaxSize(),
                            state = state,
                            onAction = onAction,
                            onConnectToPi = onConnectToPi,
                            onAPIAction = onAPIAction,
                            downloadState = downloadState,
                            onNavToDisplayScreen = onNavToDisplayScreen
                        )
                    }
                }
            }
        }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun HomePreview() {
    val dummyQuizList= listOf(
        Quiz(1, "Science quiz", null, Instant.now(), 5),
        Quiz(2, "Mathematics quiz", "Simple quiz regarding mathematics", Instant.now(), 5),
        Quiz(3, "English quiz", null, Instant.now(), 5),
    )
    SteamAppTheme { 
        HomeScreen(
            state = QuizState(localQuizzes = dummyQuizList),
            modifier = Modifier,
            onAction = {},
            onNavToEditQuizScreen = {},
            uploadState = UploadState(),
            onAPIAction = {},
            onConnectToPi = {},
            downloadState = DownloadState(),
            onNavToDisplayScreen = {},
        )
    }
}
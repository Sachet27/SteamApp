package com.example.steamapp.material_feature.presentation.home


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.steamapp.R
import com.example.steamapp.api.presentation.APIActions
import com.example.steamapp.api.presentation.UploadState
import com.example.steamapp.api.presentation.components.DownloadState
import com.example.steamapp.material_feature.domain.models.StudyMaterial
import com.example.steamapp.material_feature.presentation.MaterialActions
import com.example.steamapp.material_feature.presentation.MaterialState
import com.example.steamapp.material_feature.presentation.components.MaterialCard
import com.example.steamapp.quiz_feature.presentation.home.MyQuizzesScreen
import com.example.steamapp.quiz_feature.presentation.home.RaspberryPiQuizzesScreen
import com.example.steamapp.quiz_feature.presentation.home.TabItem
import com.example.steamapp.ui.theme.SteamAppTheme

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MaterialScreen(
    modifier: Modifier = Modifier,
    state: MaterialState,
    onMaterialAction: (MaterialActions)->Unit,
    onAPIActions: (APIActions)->Unit,
    uploadState: UploadState,
    downloadState: DownloadState,
    onNavToDisplayPdfScreen: (Boolean)->Unit
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
            TabItem.materialTabItemsList.forEachIndexed { index, tabItem ->
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
                0-> MyMaterialsScreen(
                    modifier = Modifier.fillMaxSize(),
                    state = state,
                    onMaterialAction = onMaterialAction,
                    uploadState = uploadState,
                    onAPIAction = onAPIActions,
                    onNavToDisplayPdfScreen = onNavToDisplayPdfScreen
                )
                1->{
                    RaspberryPiMaterialsScreen(
                        state = state,
                        onMaterialActions = onMaterialAction,
                        downloadState = downloadState,
                        onAPIActions = onAPIActions,
                        onNavToDisplayPdfScreen = onNavToDisplayPdfScreen
                    ) 
                }
            }
        }
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun MaterialPreview() {
    val dummyList= listOf(
        StudyMaterial(id = 1, name = "Height and Distance", description = "Class 10, chapter 7", pdfUri = "hello/world", pages = 17),
        StudyMaterial(id = 2, name = "Trigonometry", description = "Class 9, chapter 1", pdfUri = "hello/world2", pages = 20),
        StudyMaterial(id = 3, name = "History of Nepal", description = "Class 8, chapter 3", pdfUri = "hello/world", pages = 9),
    )
    SteamAppTheme {
        MaterialScreen(
            state = MaterialState(
                isLoading = false,
                myMaterials = dummyList,
                piMaterials = emptyList()
            ),
            onMaterialAction = {},
            onAPIActions = {},
            uploadState = UploadState(),
            downloadState = DownloadState(),
            modifier = Modifier,
            onNavToDisplayPdfScreen = {}
        )
    }
}
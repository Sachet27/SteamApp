package com.example.steamapp.core.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.example.steamapp.R
import com.example.steamapp.quiz_feature.presentation.components.AppBottomBar
import com.example.steamapp.quiz_feature.presentation.components.AppTopBar
import com.example.steamapp.quiz_feature.presentation.components.BottomNavItems
import com.example.steamapp.quiz_feature.presentation.components.BottomNavigationList


@Composable
fun CustomScaffold(
    modifier: Modifier = Modifier,
    selectedItem: BottomNavItems,
    onBottomItemClick: (BottomNavItems)->Unit,
    onNavToAIScreen: ()->Unit,
    userId: String?,
    onSignOut: ()->Unit,
    onStudentListClick: ()->Unit,
    onSelectItem: (BottomNavItems)-> Unit,
    content: @Composable ()->Unit
) {
    Scaffold (
        bottomBar = {
            AppBottomBar(
                onClick = {
                    onSelectItem(it)
                },
                selectedItemId = selectedItem,
                items = BottomNavigationList.list,
                onItemClick = {
                    onBottomItemClick(it.id)
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onNavToAIScreen()
                }
            ) {
                Icon(imageVector = ImageVector.vectorResource( R.drawable.ask_ai_icon ), null)
            }
        },
        topBar = {
            AppTopBar(
                title = userId,
                onProfileClick = {},
                onSignOut = onSignOut,
                userPfp = null,
                onStudentListClick = onStudentListClick,
            )
        }
    ){padding->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            content()
        }
    }
}

package com.example.steamapp.quiz_feature.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.steamapp.ui.theme.SteamAppTheme


@Composable
fun AppBottomBar(
    onClick: (BottomNavItems)-> Unit,
    selectedItemId: BottomNavItems,
    modifier: Modifier = Modifier,
    items: List<BottomNavigationItem>,
    onItemClick: (BottomNavigationItem)-> Unit
) {
    BottomAppBar(
        modifier = modifier.fillMaxWidth(),
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow
    ) {
        NavigationBar(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        ) {
            items.forEach{ item ->
                NavigationBarItem(
                    selected = selectedItemId== item.id,
                    onClick = {
                        onClick(item.id)
                        onItemClick(item)
                    },
                    icon = {
                        BadgedBox(
                            badge = {
                                if(item.badgeCount!=null){
                                    Badge{
                                        Text(
                                            text = item.badgeCount.toString()
                                        )
                                    }
                                } else if(item.hasNews){
                                    Badge()
                                }
                            }
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(
                                    if(selectedItemId==item.id) item.selectedIcon else item.unselectedIcon,
                                    ),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    },
                    label = {
                        Text(
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                            text = item.title,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    },
                    alwaysShowLabel = true
                )
            }
        }
    }
}

@Preview
@Composable
fun BottomNavPreview(modifier: Modifier = Modifier) {
    SteamAppTheme {
        Scaffold(
            bottomBar = {
                AppBottomBar(
                    items = BottomNavigationList.list,
                    onItemClick = {},
                    onClick = {},
                    selectedItemId = BottomNavItems.QUIZ
                )
            }
        ) {
            Column (modifier = Modifier
                .fillMaxSize()
                .padding(it)){
            }
        }
    }
}
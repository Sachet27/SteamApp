package com.example.steamapp.quiz_feature.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.steamapp.R
import com.example.steamapp.ui.theme.SteamAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    connected: Boolean,
    modifier: Modifier = Modifier,
    title: String?= null,
    userPfp: String?= null,
    onProfileClick: ()-> Unit,
    onSettingsClick: ()-> Unit
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        ),
        title = {
            Text(
                modifier = modifier.padding(8.dp),
                text = title?:"Guest",
                style = MaterialTheme.typography.titleLarge,
                fontSize = 25.sp
            )
        },
        navigationIcon = {
            userPfp?.let{
                // if pfp exists put here
            }?: Image(
                painter = painterResource(R.drawable.default_pfp),
                contentDescription = null,
                modifier = Modifier.padding(12.dp).clip(CircleShape).clickable {
                    onProfileClick()
                }
            )
        },
        actions = {
             Row(
                 verticalAlignment = Alignment.CenterVertically,
                 horizontalArrangement = Arrangement.spacedBy(10.dp)
             ) {
                IconButton(
                    onClick = onSettingsClick
                ) { 
                    Icon(
                        imageVector = Icons.Outlined.Settings,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(30.dp)
                    )
                }
             }
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.dot_single_svgrepo_com),
                contentDescription = null,
                tint = if(connected) Color.Green else Color.Red,
                modifier = Modifier.size(30.dp)
            )
        }
    )
}

@Preview
@Composable
private fun TopBarPreview() {
    SteamAppTheme {
        Scaffold(
            topBar = { AppTopBar(
                onProfileClick = {},
                onSettingsClick = {},
                connected = true
            ) }
        ) {
            Surface(
                modifier = Modifier.fillMaxSize().padding(it)
            ) {

            }
        }
    }
}

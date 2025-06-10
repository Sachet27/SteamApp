package com.example.steamapp.quiz_feature.presentation.components

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.steamapp.R
import com.example.steamapp.ui.theme.SteamAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    modifier: Modifier = Modifier,
    title: String?= null,
    userPfp: String?= null,
    onProfileClick: ()-> Unit,
    onSignOut: ()-> Unit,
    onStudentListClick: ()->Unit
) {
    TopAppBar(
        modifier = modifier.padding(8.dp),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
        ),
        title = {
            Row(
                modifier= modifier,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                userPfp?.let{
                    // if pfp exists put here
                }?: Box(
                    modifier = Modifier.size(42.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primary).clickable {
                        onProfileClick()
                    },
                    contentAlignment = Alignment.Center
                ){
                    Text(
                        text = title?.let { it.first().uppercase()} ?: "U",
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
                Spacer(Modifier.width(8.dp))
                Text(
                    modifier = modifier.padding(4.dp),
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Thin, fontFamily = FontFamily(Font(R.font.montserrat_medium)), fontSize = 16.sp ,letterSpacing = 0.15.sp)){
                            append("Welcome back \uD83D\uDC4B\n")
                        }
                        withStyle(
                            style = SpanStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp, fontFamily = FontFamily(Font(R.font.montserrat_medium)), letterSpacing = 0.25.sp)
                        ){
                            append(title?:"User")
                        }
                    },
                )
            }
        },
        actions = {
             Row(
                 verticalAlignment = Alignment.CenterVertically,
                 horizontalArrangement = Arrangement.spacedBy(10.dp)
             ) {
                 IconButton(
                     modifier = Modifier,
                     onClick = onStudentListClick
                 ) {
                     Icon(
                         imageVector = ImageVector.vectorResource(R.drawable.groups_24px),
                         contentDescription = null,
                         tint = MaterialTheme.colorScheme.onSurface,
                         modifier = Modifier.size(24.dp)
                     )
                 }
                IconButton(
                    modifier = Modifier,
                    onClick = onSignOut
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.logout_24px),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(24.dp)
                    )
                }
             }
        }
    )
}

//@Preview
//@Composable
//private fun TopBarPreview() {
//    SteamAppTheme {
//        Scaffold(
//            topBar = { AppTopBar(
//                onProfileClick = {},
//                onSignOut = {},
//                modifier = TODO(),
//                title = TODO(),
//                userPfp = TODO(),
//                onStudentListClick = TODO(),
//            ) }
//        ) {
//            Surface(
//                modifier = Modifier.fillMaxSize().padding(it)
//            ) {
//
//            }
//        }
//    }
//}

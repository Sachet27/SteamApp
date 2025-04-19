package com.example.steamapp.quiz_feature.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavigationItem(
    val id: BottomNavItems,
    val title: String,
    @DrawableRes val unselectedIcon: Int,
    @DrawableRes val selectedIcon: Int,
    val hasNews: Boolean= false,
    val badgeCount: Int?= null
)

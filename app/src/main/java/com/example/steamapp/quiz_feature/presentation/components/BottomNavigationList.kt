package com.example.steamapp.quiz_feature.presentation.components

object BottomNavigationList {
    val list= BottomNavItems.list.map {
        BottomNavigationItem(
            id = it,
            title = it.toTitle(),
            unselectedIcon = it.toUnselectedIcon(),
            selectedIcon = it.toSelectedIcon(),
        )
    }
}
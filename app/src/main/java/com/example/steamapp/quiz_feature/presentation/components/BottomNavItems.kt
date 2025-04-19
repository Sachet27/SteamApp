package com.example.steamapp.quiz_feature.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.steamapp.R

enum class BottomNavItems {
    QUIZ, MATERIAL;
    companion object{
        val list= listOf(QUIZ, MATERIAL)
    }
}

fun BottomNavItems.toTitle(): String{
    return when(this){
        BottomNavItems.QUIZ-> "Quizzes"
        BottomNavItems.MATERIAL-> "Materials"
    }
}

fun BottomNavItems.toUnselectedIcon(): Int{
    return when(this){
        BottomNavItems.QUIZ-> R.drawable.quiz_icon_outlined
        BottomNavItems.MATERIAL-> R.drawable.library_icon_outlined
    }
}

fun BottomNavItems.toSelectedIcon(): Int{
    return when(this){
        BottomNavItems.QUIZ-> R.drawable.quiz_icon_filled
        BottomNavItems.MATERIAL-> R.drawable.library_icon_filled
    }
}
package com.example.steamapp.quiz_feature.presentation.components

import com.example.steamapp.R

enum class BottomNavItems {
    QUIZ, MATERIAL, CREATE;
    companion object{
        val list= listOf(QUIZ, CREATE, MATERIAL)
        val studentList= listOf(QUIZ,MATERIAL)
    }
}

fun BottomNavItems.toTitle(): String{
    return when(this){
        BottomNavItems.QUIZ-> "Quizzes"
        BottomNavItems.MATERIAL-> "Materials"
        BottomNavItems.CREATE -> "Create"
    }
}

fun BottomNavItems.toUnselectedIcon(): Int{
    return when(this){
        BottomNavItems.QUIZ-> R.drawable.quiz_icon_outlined
        BottomNavItems.MATERIAL-> R.drawable.library_icon_outlined
        BottomNavItems.CREATE -> R.drawable.create_icon
    }
}

fun BottomNavItems.toSelectedIcon(): Int{
    return when(this){
        BottomNavItems.QUIZ-> R.drawable.quiz_icon_filled
        BottomNavItems.MATERIAL-> R.drawable.library_icon_filled
        BottomNavItems.CREATE -> R.drawable.create_icon
    }
}
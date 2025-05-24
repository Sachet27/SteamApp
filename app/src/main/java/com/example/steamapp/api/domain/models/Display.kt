package com.example.steamapp.api.domain.models

data class Display(
    val quizName: String,
    val displayMode: DisplayMode= DisplayMode.FULL_SCREEN
)

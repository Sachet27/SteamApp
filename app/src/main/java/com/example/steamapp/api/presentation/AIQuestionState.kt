package com.example.steamapp.api.presentation


data class AIQuestionState(
    val isLoading: Boolean= false,
    val question: String?= null,
    val answer: String? = null,
    val think: Boolean= false
)
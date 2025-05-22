package com.example.steamapp.quiz_feature.presentation

import com.example.steamapp.quiz_feature.domain.models.Quiz

data class QuizState(
    val connectedToPi: Boolean= true,
    val localQuizzes: List<Quiz> = emptyList(),
    val remoteQuizzes: List<Quiz> = emptyList(),
    val isLoading: Boolean= false,
)

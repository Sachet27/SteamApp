package com.example.steamapp.quiz_feature.presentation

import com.example.steamapp.quiz_feature.data.local.entities.relations.QuizWithQuestions
import com.example.steamapp.quiz_feature.domain.models.Quiz

data class QuizState(
    val connectedToPi: Boolean= false,
    val localQuizzes: List<Quiz> = emptyList(),
    val remoteQuizzes: List<Quiz> = emptyList(),
    val isLoading: Boolean= false,
    val pushedQuiz: QuizWithQuestions? = null
)

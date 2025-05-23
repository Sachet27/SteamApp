package com.example.steamapp.api.data.networking.dto

import kotlinx.serialization.Serializable

@Serializable
data class QuizListDto(
    val quizzes: List<QuizDto>
)

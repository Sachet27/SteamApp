package com.example.steamapp.api.data.networking.dto

import kotlinx.serialization.Serializable

@Serializable
data class QuizDto(
    val quizId: Long,
    val title: String,
    val description: String?,
    val lastUpdatedAt: Long,
    val questionCount: Int
)

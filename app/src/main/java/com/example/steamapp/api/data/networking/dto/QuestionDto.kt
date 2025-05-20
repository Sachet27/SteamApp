package com.example.steamapp.api.data.networking.dto

import kotlinx.serialization.Serializable

@Serializable
data class QuestionDto(
    val id: Long,
    val title: String,
    val options: List<String>,
    val correctOptionIndex: Int,
    val imageRelativePath: String?,
    val audioRelativePath: String?,
    val quizId: Long
)

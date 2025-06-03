package com.example.steamapp.api.data.networking.dto

import kotlinx.serialization.Serializable


@Serializable
data class AnswerStyleResponseDto(
    val user_id: String,
    val answerStyle: String
)

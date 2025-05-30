package com.example.steamapp.api.data.networking.dto

import kotlinx.serialization.Serializable


@Serializable
data class AIAnswerDto(
    val user_id: String,
    val question: String,
    val answer: String
)

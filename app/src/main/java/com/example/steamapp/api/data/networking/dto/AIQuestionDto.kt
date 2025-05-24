package com.example.steamapp.api.data.networking.dto

import kotlinx.serialization.Serializable

@Serializable
data class AIQuestionDto (
    val user_id: String,
    val question: String
)
package com.example.steamapp.api.data.networking.dto

import kotlinx.serialization.Serializable

@Serializable
data class DisplayDto(
    val quizId: Long,
    val displayMode: String
)

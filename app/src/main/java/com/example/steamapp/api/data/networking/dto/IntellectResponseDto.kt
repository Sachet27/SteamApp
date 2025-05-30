package com.example.steamapp.api.data.networking.dto

import kotlinx.serialization.Serializable


@Serializable
data class IntellectResponseDto(
    val user_id: String,
    val intellect: String
)

package com.example.steamapp.api.data.networking.dto

import kotlinx.serialization.Serializable

@Serializable
data class UploadResponseDto(
    val details: FileDetailsDto,
    val message: String?
)

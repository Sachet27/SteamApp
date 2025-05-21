package com.example.steamapp.api.data.networking.dto

import kotlinx.serialization.Serializable

@Serializable
data class FileDetailsDto (
    val files: List<String>,
    val media_files: List<String>,
    val questions: List<String>
)
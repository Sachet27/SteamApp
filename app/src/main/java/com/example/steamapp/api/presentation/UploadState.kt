package com.example.steamapp.api.presentation

import com.example.steamapp.api.domain.models.UploadResponse

data class UploadState(
    val isUploading: Boolean= false,
    val isUploadComplete: Boolean= false,
    val progress: Float= 0f,
    val uploadResponse: UploadResponse = UploadResponse(),
    val error: String?= null
)
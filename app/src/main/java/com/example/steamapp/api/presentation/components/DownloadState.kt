package com.example.steamapp.api.presentation.components

import com.example.steamapp.api.domain.models.UploadResponse

data class DownloadState(
    val isUploading: Boolean= false,
    val isUploadComplete: Boolean= false,
    val progress: Float= 0f,
    val error: String?= null
)

package com.example.steamapp.api.domain.models

data class UploadResponse (
    val details: FileDetails= FileDetails(emptyList(), emptyList(), emptyList()),
    val message: String?= null
)
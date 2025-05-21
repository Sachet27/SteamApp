package com.example.steamapp.api.data.mappers

import com.example.steamapp.api.data.networking.dto.FileDetailsDto
import com.example.steamapp.api.data.networking.dto.UploadResponseDto
import com.example.steamapp.api.domain.models.FileDetails
import com.example.steamapp.api.domain.models.UploadResponse

fun FileDetailsDto.toFileDetails(): FileDetails{
    return FileDetails(
        files = files,
        media_files = media_files,
        questions = questions
    )
}

fun UploadResponseDto.toUploadResponse(): UploadResponse{
    return UploadResponse(
        details = details.toFileDetails(),
        message = message
    )
}
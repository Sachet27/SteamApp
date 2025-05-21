package com.example.steamapp.core.util.networking

import com.example.steamapp.api.data.networking.models.ProgressUpdate

sealed class UploadStatus <out T, out E: Error> {
    data class Progress(val progress: ProgressUpdate): UploadStatus<Nothing, Nothing>()
    data class ResultState <out T, out E: Error> (val result: Result<T,E>): UploadStatus<T,E>()
}
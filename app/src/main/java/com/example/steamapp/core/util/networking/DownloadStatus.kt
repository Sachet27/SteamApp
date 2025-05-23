package com.example.steamapp.core.util.networking

import com.example.steamapp.api.data.networking.models.ProgressUpdate

sealed class DownloadStatus <out T, out E: Error> {
    data class Progress(val progress: ProgressUpdate): DownloadStatus<Nothing, Nothing>()
    data class ResultState <out T, out E: Error> (val result: Result<T,E>): DownloadStatus<T,E>()
}
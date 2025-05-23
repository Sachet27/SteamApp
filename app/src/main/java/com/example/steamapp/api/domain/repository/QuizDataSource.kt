package com.example.steamapp.api.domain.repository

import android.view.Display
import com.example.steamapp.api.domain.models.ControlMode
import com.example.steamapp.api.data.networking.dto.UploadResponseDto
import com.example.steamapp.core.util.networking.DownloadStatus
import com.example.steamapp.core.util.networking.EmptyResult
import com.example.steamapp.core.util.networking.NetworkError
import com.example.steamapp.core.util.networking.Result
import com.example.steamapp.core.util.networking.UploadStatus
import com.example.steamapp.quiz_feature.data.local.entities.relations.QuizWithQuestions
import com.example.steamapp.quiz_feature.domain.models.Quiz
import kotlinx.coroutines.flow.Flow

interface QuizDataSource {
    suspend fun getQuizzes(): Result<List<Quiz>, NetworkError>
    fun getQuizWithQuestions(quizId: Long, rawQuizName: String): Flow<DownloadStatus<Unit, NetworkError>>
    fun pushQuizWithQuestions(quizWithQuestions: QuizWithQuestions): Flow<UploadStatus<UploadResponseDto, NetworkError>>
    suspend fun pushAction(controlMode: ControlMode): EmptyResult<NetworkError>
    suspend fun pushDisplay(display: Display): EmptyResult<NetworkError>
    suspend fun deleteQuizByQuizId(quizId: Long): EmptyResult<NetworkError>
}
package com.example.steamapp.api.domain.repository

import com.example.steamapp.api.domain.models.AIAnswer
import com.example.steamapp.api.domain.models.AIQuestion
import com.example.steamapp.api.domain.models.IntellectRequest
import com.example.steamapp.api.domain.models.IntellectResponse
import com.example.steamapp.core.util.networking.NetworkError
import com.example.steamapp.core.util.networking.Result

interface AIDataSource {
    suspend fun askQuestion(question: AIQuestion): Result<AIAnswer, NetworkError>
    suspend fun selectIntellectLevel(intellect: IntellectRequest, user_id: String): Result<IntellectResponse, NetworkError>
}
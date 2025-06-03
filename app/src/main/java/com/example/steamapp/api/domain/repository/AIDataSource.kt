package com.example.steamapp.api.domain.repository

import com.example.steamapp.api.domain.models.AIAnswer
import com.example.steamapp.api.domain.models.AIQuestion
import com.example.steamapp.api.domain.models.AnswerStyle
import com.example.steamapp.api.domain.models.AnswerStyleRequest
import com.example.steamapp.api.domain.models.AnswerStyleResponse
import com.example.steamapp.core.util.networking.NetworkError
import com.example.steamapp.core.util.networking.Result

interface AIDataSource {
    suspend fun askQuestion(question: AIQuestion): Result<AIAnswer, NetworkError>
}
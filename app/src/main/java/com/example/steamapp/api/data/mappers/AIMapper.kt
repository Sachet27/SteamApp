package com.example.steamapp.api.data.mappers

import com.example.steamapp.api.data.networking.dto.AIAnswerDto
import com.example.steamapp.api.data.networking.dto.AIQuestionDto
import com.example.steamapp.api.data.networking.dto.IntellectRequestDto
import com.example.steamapp.api.data.networking.dto.IntellectResponseDto
import com.example.steamapp.api.domain.models.AIAnswer
import com.example.steamapp.api.domain.models.AIQuestion
import com.example.steamapp.api.domain.models.Intellect
import com.example.steamapp.api.domain.models.IntellectRequest
import com.example.steamapp.api.domain.models.IntellectResponse

fun AIQuestion.toAIQuestionDTO(): AIQuestionDto{
    return AIQuestionDto(
        user_id = userId,
        question = question
    )
}

fun AIAnswerDto.toAIAnswer(): AIAnswer{
    return AIAnswer(
        userId = user_id,
        question = question,
        answer = answer
    )
}

fun IntellectRequest.toIntellectRequestDTO(): IntellectRequestDto{
    return IntellectRequestDto(
        intellect = when(intellect){
            Intellect.LOW-> "low"
            Intellect.NORMAL -> "normal"
            Intellect.HIGH -> "high"
        }
    )
}

fun IntellectResponseDto.toIntellectResponse(): IntellectResponse{
    return IntellectResponse(
        user_id = user_id,
        intellect = when(intellect){
            "low"-> Intellect.LOW
            "normal"-> Intellect.NORMAL
            "high"-> Intellect.HIGH
            else -> Intellect.NORMAL
        }
    )
}
package com.example.steamapp.api.data.mappers

import com.example.steamapp.api.data.networking.dto.AIAnswerDto
import com.example.steamapp.api.data.networking.dto.AIQuestionDto
import com.example.steamapp.api.domain.models.AIAnswer
import com.example.steamapp.api.domain.models.AIQuestion

fun AIQuestion.toAIQuestionDTO(): AIQuestionDto{
    return AIQuestionDto(
        user_id = userId,
        question = question,
        think = think,
    )
}

fun AIAnswerDto.toAIAnswer(): AIAnswer{
    return AIAnswer(
        userId = user_id,
        question = question,
        answer = answer.trim()
            .replace("<think>\n\n</think>\n\n", "")
    )
}

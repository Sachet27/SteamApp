package com.example.steamapp.api.data.networking.dto


import kotlinx.serialization.Serializable

@Serializable
data class QuizWithQuestionsDto(
    val quiz: QuizDto,
    val questions: List<QuestionDto>
)

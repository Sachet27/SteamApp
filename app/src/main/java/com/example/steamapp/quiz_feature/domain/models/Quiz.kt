package com.example.steamapp.quiz_feature.domain.models

import java.time.Instant

data class Quiz(
    val quizId: Long,
    val title: String,
    val description: String?,
    val lastUpdatedAt: Instant,
    val questionCount: Int
)

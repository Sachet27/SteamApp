package com.example.steamapp.quiz_feature.domain.models

data class Question(
    val id: Long,
    val title: String,
    val options: List<String>,
    val correctOptionIndex: Int= 0,
    val imageRelativePath: String?,
    val audioRelativePath: String?,
    val quizId: Long
)

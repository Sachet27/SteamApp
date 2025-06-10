package com.example.steamapp.student

import kotlinx.serialization.Serializable

@Serializable
data class SubjectScores(
    val Maths: Float,
    val Science: Float,
    val Nepali: Float,
    val English: Float,
    val Social: Float
)

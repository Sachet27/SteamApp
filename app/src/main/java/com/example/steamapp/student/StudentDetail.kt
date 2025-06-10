package com.example.steamapp.student

import kotlinx.serialization.Serializable

@Serializable
data class StudentDetail(
    val name: String,
    val rating: Float,
    val classification: String,
    val strengths: List<String>,
    val weaknesses: List<String>,
    val subject_ratios: SubjectScores
)

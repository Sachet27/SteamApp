package com.example.steamapp.material_feature.domain.models

import com.example.steamapp.student.quiz.domain.models.MaterialType
import kotlinx.serialization.Serializable

@Serializable
data class StudyMaterial(
    val id: Long,
    val name: String,
    val description: String?,
    val materialType: MaterialType= MaterialType.PDF,
    val pdfUri: String,
    val pages: Int,
)

package com.example.steamapp.material_feature.domain.models

data class StudyMaterial(
    val id: Long,
    val name: String,
    val description: String?,
    val pdfUri: String,
    val pages: Int
)

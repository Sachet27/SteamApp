package com.example.steamapp.material_feature.presentation

import com.example.steamapp.material_feature.domain.models.StudyMaterial

data class MaterialState(
    val isLoading: Boolean= false,
    val materials: List<StudyMaterial> = emptyList(),
    val selectedMaterial: StudyMaterial?= null
)

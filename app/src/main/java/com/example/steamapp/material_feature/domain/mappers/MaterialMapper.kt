package com.example.steamapp.material_feature.domain.mappers

import com.example.steamapp.material_feature.data.local.entities.StudyMaterialEntity
import com.example.steamapp.material_feature.domain.models.StudyMaterial

fun StudyMaterial.toStudyMaterialEntity(): StudyMaterialEntity{
    return StudyMaterialEntity(
        id = id,
        name = name,
        description = description,
        pdfUri = pdfUri,
        pages = pages
    )
}

fun StudyMaterialEntity.toStudyMaterial(): StudyMaterial{
    return StudyMaterial(
        id = id,
        name = name,
        description = description,
        pdfUri = pdfUri,
        pages = pages
    )
}
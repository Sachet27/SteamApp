package com.example.steamapp.material_feature.domain.repository

import com.example.steamapp.material_feature.domain.models.StudyMaterial
import kotlinx.coroutines.flow.Flow

interface MaterialRepository {
        suspend fun insertMaterial(material: StudyMaterial): Long

        fun getAllMaterials(): Flow<List<StudyMaterial>>

        suspend fun getMaterialById(id: Long): StudyMaterial?

        suspend fun updateMaterial(material: StudyMaterial)

        suspend fun deleteMaterial(material: StudyMaterial)
}
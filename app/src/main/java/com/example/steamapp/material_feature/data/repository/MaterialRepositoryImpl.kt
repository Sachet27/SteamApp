package com.example.steamapp.material_feature.data.repository

import com.example.steamapp.material_feature.data.local.dao.MaterialDao
import com.example.steamapp.material_feature.domain.mappers.toStudyMaterial
import com.example.steamapp.material_feature.domain.mappers.toStudyMaterialEntity
import com.example.steamapp.material_feature.domain.models.StudyMaterial
import com.example.steamapp.material_feature.domain.repository.MaterialRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MaterialRepositoryImpl(
    private val dao: MaterialDao
): MaterialRepository {
    override suspend fun insertMaterial(material: StudyMaterial): Long {
      return dao.insertMaterial(material.toStudyMaterialEntity())
    }

    override fun getAllMaterials(): Flow<List<StudyMaterial>> {
        return dao.getAllMaterials().map { materials->
            materials.map {
                it.toStudyMaterial()
            }
        }
    }

    override suspend fun getMaterialById(id: Long): StudyMaterial? {
        return dao.getMaterialById(id)?.toStudyMaterial()
    }

    override suspend fun updateMaterial(material: StudyMaterial) {
        dao.updateMaterial(material.toStudyMaterialEntity())
    }

    override suspend fun deleteMaterial(material: StudyMaterial) {
        dao.deleteMaterial(material.toStudyMaterialEntity())
    }

}
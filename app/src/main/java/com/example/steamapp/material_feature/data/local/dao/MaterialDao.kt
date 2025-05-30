package com.example.steamapp.material_feature.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.example.steamapp.material_feature.data.local.entities.StudyMaterialEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MaterialDao {
    @Upsert
    suspend fun insertMaterial(studyMaterial: StudyMaterialEntity): Long

    @Query("Select * FROM StudyMaterialEntity")
    fun getAllMaterials(): Flow<List<StudyMaterialEntity>>

    @Query("Select * FROM StudyMaterialEntity WHERE id= :id")
    suspend fun getMaterialById(id: Long): StudyMaterialEntity?

    @Update
    suspend fun updateMaterial(material: StudyMaterialEntity)

    @Delete
    suspend fun deleteMaterial(material: StudyMaterialEntity)
}
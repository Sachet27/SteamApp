package com.example.steamapp.material_feature.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity
data class StudyMaterialEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long= 0L,
    val name: String,
    val description: String?,
    val pdfUri: String,
    val pages: Int
)

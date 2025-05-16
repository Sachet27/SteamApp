package com.example.steamapp.quiz_feature.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity
data class QuestionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long= 0L,
    val title: String,
    val options: List<String>,
    val correctOptionIndex: Int,
    val imageRelativePath: String?,
    val audioRelativePath: String?,
    val quizId: Long
)

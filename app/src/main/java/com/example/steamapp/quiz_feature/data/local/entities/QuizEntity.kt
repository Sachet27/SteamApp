package com.example.steamapp.quiz_feature.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.steamapp.core.data.internal_storage.converters.InstantSerializer
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
@Entity
data class QuizEntity(
    @PrimaryKey(autoGenerate = true)
    val quizId: Long= 0L,
    val title: String,
    val description: String?,
    @Serializable(with = InstantSerializer::class)
    val lastUpdatedAt: Instant,
    val questionCount: Int
)


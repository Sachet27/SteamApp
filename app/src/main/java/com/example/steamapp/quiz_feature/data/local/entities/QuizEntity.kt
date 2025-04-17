package com.example.steamapp.quiz_feature.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

@Entity
data class QuizEntity(
    @PrimaryKey(autoGenerate = true)
    val quizId: Long= 0L,
    val title: String,
    val description: String?,
    val lastUpdatedAt: Instant,
    val questionCount: Int
)


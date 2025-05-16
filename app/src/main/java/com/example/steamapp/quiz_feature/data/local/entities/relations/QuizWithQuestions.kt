package com.example.steamapp.quiz_feature.data.local.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.steamapp.quiz_feature.data.local.entities.QuestionEntity
import com.example.steamapp.quiz_feature.data.local.entities.QuizEntity
import kotlinx.serialization.Serializable

@Serializable
data class QuizWithQuestions(
    @Embedded val quiz: QuizEntity,
    @Relation(
        parentColumn = "quizId",
        entityColumn = "quizId"
    )
    val questions: List<QuestionEntity>
)

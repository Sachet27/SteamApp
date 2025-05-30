package com.example.steamapp.api.data.mappers

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.steamapp.api.data.networking.dto.QuestionDto
import com.example.steamapp.api.data.networking.dto.QuizDto
import com.example.steamapp.api.data.networking.dto.QuizWithQuestionsDto
import com.example.steamapp.api.data.networking.dto.ScoreDto
import com.example.steamapp.api.domain.models.Score
import com.example.steamapp.quiz_feature.data.local.entities.QuestionEntity
import com.example.steamapp.quiz_feature.data.local.entities.QuizEntity
import com.example.steamapp.quiz_feature.data.local.entities.relations.QuizWithQuestions
import java.time.Instant

@RequiresApi(Build.VERSION_CODES.O)
fun QuizDto.toQuizEntity(): QuizEntity{
    return QuizEntity(
        quizId = quizId,
        title = title,
        description = description,
        lastUpdatedAt = Instant.ofEpochMilli(lastUpdatedAt),
        questionCount = questionCount
    )
}

fun QuestionDto.toQuestionEntity(): QuestionEntity{
    return QuestionEntity(
        id = id,
        title = title,
        options = options,
        correctOptionIndex = correctOptionIndex,
        imageRelativePath = imageRelativePath,
        audioRelativePath = audioRelativePath,
        quizId = quizId
    )
}

@RequiresApi(Build.VERSION_CODES.O)
fun QuizWithQuestionsDto.toQuizWithQuestions(): QuizWithQuestions{
    return QuizWithQuestions(
        quiz = quiz.toQuizEntity(),
        questions = questions.map {
            it.toQuestionEntity()
        }
    )
}

fun Score.toScoreDto(): ScoreDto{
    return ScoreDto(
        Sachet = Sachet,
        Nidhi = Nidhi,
        Anjal = Anjal
    )
}

fun ScoreDto.toScore(): Score{
    return Score(
        Sachet = Sachet,
        Nidhi = Nidhi,
        Anjal = Anjal
    )
}


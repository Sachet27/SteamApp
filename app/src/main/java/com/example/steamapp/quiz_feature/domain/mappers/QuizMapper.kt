package com.example.steamapp.quiz_feature.domain.mappers

import com.example.steamapp.quiz_feature.data.local.entities.QuestionEntity
import com.example.steamapp.quiz_feature.data.local.entities.QuizEntity
import com.example.steamapp.quiz_feature.data.local.entities.relations.QuizWithQuestions
import com.example.steamapp.quiz_feature.domain.models.Question
import com.example.steamapp.quiz_feature.domain.models.Quiz

fun QuizEntity.toQuiz(): Quiz{
    return Quiz(
        quizId = quizId,
        title = title,
        description = description,
        lastUpdatedAt = lastUpdatedAt,
        questionCount = questionCount
    )
}

fun Quiz.toQuizEntity(): QuizEntity{
    return QuizEntity(
        quizId = quizId,
        title = title,
        description = description,
        lastUpdatedAt = lastUpdatedAt,
        questionCount = questionCount
    )
}

fun Question.toQuestionEntity(): QuestionEntity{
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

fun QuestionEntity.toQuestion(): Question{
    return Question(
        id = id,
        title = title,
        options = options,
        correctOptionIndex = correctOptionIndex,
        imageRelativePath = imageRelativePath,
        audioRelativePath = audioRelativePath,
        quizId = quizId
    )
}


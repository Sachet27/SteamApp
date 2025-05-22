package com.example.steamapp.quiz_feature.domain.repository

import com.example.steamapp.quiz_feature.data.local.entities.relations.QuizWithQuestions
import com.example.steamapp.quiz_feature.domain.models.Quiz
import kotlinx.coroutines.flow.Flow

interface QuizRepository{
    suspend fun insertQuizWithQuestions(quizWithQuestions: QuizWithQuestions): Long

    fun getAllQuizzes(): Flow<List<Quiz>>
    suspend fun getQuizWithQuestionsById(quizId: Long): QuizWithQuestions?

    suspend fun updateQuizWithQuestions(quizWithQuestions: QuizWithQuestions)

    suspend fun deleteQuizWithQuestionsByQuizId(quizId: Long)
}
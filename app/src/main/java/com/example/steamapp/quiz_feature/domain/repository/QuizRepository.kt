package com.example.steamapp.quiz_feature.domain.repository

import com.example.steamapp.quiz_feature.data.local.entities.relations.QuizWithQuestions
import com.example.steamapp.quiz_feature.domain.models.Question
import com.example.steamapp.quiz_feature.domain.models.Quiz
import kotlinx.coroutines.flow.Flow

interface QuizRepository{
    suspend fun insertQuiz(quiz: Quiz)
    suspend fun insertQuestion(question: Question)
    fun getAllQuizzes(): Flow<List<Quiz>>
    fun getQuestionsByQuizId(quizId: Long): Flow<List<Question>>

    //add a model and mapper for this in domain
    suspend fun getQuizWithQuestionsById(quizId: Long): QuizWithQuestions?

    suspend fun updateQuestion(question: Question)
    suspend fun deleteQuizWithQuestionsByQuizId(quizId: Long)
    suspend fun deleteQuestionById(id: Long)
    suspend fun deleteQuizById(quizId: Long)
    suspend fun deleteQuestionsByQuizId(quizId: Long)
}
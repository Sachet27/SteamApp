package com.example.steamapp.quiz_feature.data.repository

import com.example.steamapp.quiz_feature.data.local.dao.QuizDao
import com.example.steamapp.quiz_feature.data.local.entities.relations.QuizWithQuestions
import com.example.steamapp.quiz_feature.domain.mappers.toQuestion
import com.example.steamapp.quiz_feature.domain.mappers.toQuestionEntity
import com.example.steamapp.quiz_feature.domain.mappers.toQuiz
import com.example.steamapp.quiz_feature.domain.mappers.toQuizEntity
import com.example.steamapp.quiz_feature.domain.models.Question
import com.example.steamapp.quiz_feature.domain.models.Quiz
import com.example.steamapp.quiz_feature.domain.repository.QuizRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class QuizRepositoryImpl(
    private val dao: QuizDao
): QuizRepository {
    override suspend fun insertQuiz(quiz: Quiz) {
        dao.insertQuiz(quiz.toQuizEntity())
    }

    override suspend fun insertQuestion(question: Question) {
        dao.insertQuestion(question.toQuestionEntity())
    }

    override fun getAllQuizzes(): Flow<List<Quiz>> {
        return dao.getAllQuizzes().map { quizzes->
            quizzes.map {
                it.toQuiz()
            }
        }
    }

    override fun getQuestionsByQuizId(quizId: Long): Flow<List<Question>> {
        return dao.getQuestionsByQuizId(quizId).map { questions->
            questions.map {
                it.toQuestion()
            }
        }
    }

    override suspend fun getQuizWithQuestionsById(quizId: Long): QuizWithQuestions? {
        return dao.getQuizWithQuestionsById(quizId)
    }

    override suspend fun updateQuestion(question: Question) {
        dao.updateQuestion(question.toQuestionEntity())
    }

    override suspend fun deleteQuizWithQuestionsByQuizId(quizId: Long) {
        dao.deleteQuizWithQuestionsByQuizId(quizId)
    }

    override suspend fun deleteQuestionById(id: Long) {
        dao.deleteQuestionById(id)
    }

    override suspend fun deleteQuizById(quizId: Long) {
        dao.deleteQuizById(quizId)
    }

    override suspend fun deleteQuestionsByQuizId(quizId: Long) {
        dao.deleteQuestionsByQuizId(quizId)
    }

}
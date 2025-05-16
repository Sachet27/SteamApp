package com.example.steamapp.quiz_feature.data.repository

import com.example.steamapp.quiz_feature.data.local.dao.QuizDao
import com.example.steamapp.quiz_feature.data.local.entities.relations.QuizWithQuestions
import com.example.steamapp.quiz_feature.domain.mappers.toQuiz
import com.example.steamapp.quiz_feature.domain.models.Quiz
import com.example.steamapp.quiz_feature.domain.repository.QuizRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class QuizRepositoryImpl(
    private val dao: QuizDao
): QuizRepository {

    override suspend fun insertQuizWithQuestions(quizWithQuestions: QuizWithQuestions):Long {
        return dao.insertQuizWithQuestions(quizWithQuestions)
    }

    override fun getAllQuizzes(): Flow<List<Quiz>> {
        return dao.getAllQuizzes().map { quizzes->
            quizzes.map {
                it.toQuiz()
            }
        }
    }

    override suspend fun getQuizWithQuestionsById(quizId: Long): QuizWithQuestions? {
        return dao.getQuizWithQuestionsById(quizId)
    }


    override suspend fun updateQuizWithQuestions(quizWithQuestions: QuizWithQuestions) {
        dao.updateQuizWithQuestions(quizWithQuestions)
    }

    override suspend fun deleteQuizWithQuestionsByQuizId(quizId: Long) {
        dao.deleteQuizWithQuestionsByQuizId(quizId)
    }

}
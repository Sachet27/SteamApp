package com.example.steamapp.quiz_feature.data.local.dao

import android.view.Display
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
import com.example.steamapp.quiz_feature.data.local.entities.QuestionEntity
import com.example.steamapp.quiz_feature.data.local.entities.QuizEntity
import com.example.steamapp.quiz_feature.data.local.entities.relations.QuizWithQuestions
import kotlinx.coroutines.flow.Flow

@Dao
interface QuizDao {
    //CREATE
    @Upsert
    suspend fun insertQuiz(quiz:QuizEntity):Long

    @Upsert
    suspend fun insertQuestions(questions: List<QuestionEntity>)

    @Transaction
    suspend fun insertQuizWithQuestions(quizWithQuestions: QuizWithQuestions): Long{
        val quizId= insertQuiz(quizWithQuestions.quiz)
        val questions= quizWithQuestions.questions.map {
            it.copy(
                quizId = quizId
            )
        }
        insertQuestions(questions)
        return quizId
    }

    // READ
    @Query("SELECT * FROM QuizEntity")
    fun getAllQuizzes(): Flow<List<QuizEntity>>

    @Query("SELECT * FROM QuestionEntity WHERE quizId= :quizId ")
    fun getQuestionsByQuizId(quizId: Long): Flow<List<QuestionEntity>>

    @Transaction
    @Query("SELECT * FROM QuizEntity WHERE quizId= :quizId")
    suspend fun getQuizWithQuestionsById(quizId: Long): QuizWithQuestions?


    //Update
    @Transaction
    suspend fun updateQuizWithQuestions(quizWithQuestions: QuizWithQuestions){
        insertQuiz(quizWithQuestions.quiz)

        deleteQuestionsByQuizId(quizWithQuestions.quiz.quizId)

        val updatedQuestions= quizWithQuestions.questions.map {
            it.copy(
                quizId = quizWithQuestions.quiz.quizId
            )
        }
        insertQuestions(updatedQuestions)
    }




    //DELETE
    @Transaction
    suspend fun deleteQuizWithQuestionsByQuizId(quizId: Long){
        deleteQuestionsByQuizId(quizId)
        deleteQuizById(quizId)
    }

    @Query("DELETE FROM QuizEntity WHERE quizId= :quizId")
    suspend fun deleteQuizById(quizId: Long)

    @Query("DELETE FROM QuestionEntity WHERE quizId= :quizId")
    suspend fun deleteQuestionsByQuizId(quizId: Long)
}


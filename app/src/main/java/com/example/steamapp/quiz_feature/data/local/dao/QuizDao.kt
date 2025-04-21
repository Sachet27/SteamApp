package com.example.steamapp.quiz_feature.data.local.dao

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
    suspend fun insertQuestion(question: QuestionEntity)


    // READ
    @Query("SELECT * FROM QuizEntity")
    fun getAllQuizzes(): Flow<List<QuizEntity>>

    @Query("SELECT * FROM QuestionEntity WHERE quizId= :quizId ")
    fun getQuestionsByQuizId(quizId: Long): Flow<List<QuestionEntity>>

    @Transaction
    @Query("SELECT * FROM QuizEntity WHERE quizId= :quizId")
    suspend fun getQuizWithQuestionsById(quizId: Long): QuizWithQuestions?


    //Update
    @Update
    suspend fun updateQuestion(question: QuestionEntity)

    @Update
    suspend fun updateQuiz(quiz: QuizEntity)


    //DELETE
    @Transaction
    suspend fun deleteQuizWithQuestionsByQuizId(quizId: Long){
        deleteQuestionsByQuizId(quizId)
        deleteQuizById(quizId)
    }


    @Query("DELETE FROM QuestionEntity WHERE id= :id")
    suspend fun deleteQuestionById(id: Long)

    @Query("DELETE FROM QuizEntity WHERE quizId= :quizId")
    suspend fun deleteQuizById(quizId: Long)

    @Query("DELETE FROM QuestionEntity WHERE quizId= :quizId")
    suspend fun deleteQuestionsByQuizId(quizId: Long)
}
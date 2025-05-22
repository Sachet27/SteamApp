package com.example.steamapp.quiz_feature.presentation

import com.example.steamapp.quiz_feature.data.local.entities.relations.QuizWithQuestions
import com.example.steamapp.quiz_feature.domain.models.Question
import com.example.steamapp.quiz_feature.domain.models.Quiz

sealed class QuizActions {
    data class onSelectQuiz(val quizId: Long): QuizActions()
    data object onClearSelectedQuiz: QuizActions()
    data class onChangeQuestion(val question: Question): QuizActions()
    data class onDeleteQuiz(val quizId: Long, val quizName: String): QuizActions()
    data class onInsertQuiz(val quizWithQuestions: QuizWithQuestions): QuizActions()
    data class onUpdateQuiz(val quizWithQuestions: QuizWithQuestions): QuizActions()
    data class onLoadQuizData(val quizId: Long?): QuizActions()
    data object onClearData: QuizActions()
    data object onClearFormData: QuizActions()
    data class onChangeTitle(val newTitle: String): QuizActions()
    data class onChangeOptionA(val newOptionA: String): QuizActions()
    data class onChangeOptionB(val newOptionB: String): QuizActions()
    data class onChangeOptionC(val newOptionC: String): QuizActions()
    data class onChangeOptionD(val newOptionD: String): QuizActions()
    data class onChangeAnswerIndex(val index: Int): QuizActions()
    data class onChangeQuizTitle(val newQuizTitle: String): QuizActions()
    data class onChangeQuizDescription(val newQuizDescription: String): QuizActions()
}
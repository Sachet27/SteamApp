package com.example.steamapp.quiz_feature.presentation

import com.example.steamapp.quiz_feature.data.local.entities.relations.QuizWithQuestions

sealed class QuizActions {
    data object onClearData: QuizActions()
    data class onAddQuiz(val quizWithQuestions: QuizWithQuestions): QuizActions()
    data class onUpdateQuiz(val quizWithQuestions: QuizWithQuestions): QuizActions()
    data class onChangeTitle(val newTitle: String): QuizActions()
    data class onChangeOptionA(val newOptionA: String): QuizActions()
    data class onChangeOptionB(val newOptionB: String): QuizActions()
    data class onChangeOptionC(val newOptionC: String): QuizActions()
    data class onChangeOptionD(val newOptionD: String): QuizActions()
    data class onChangeAnswerIndex(val index: Int): QuizActions()
}
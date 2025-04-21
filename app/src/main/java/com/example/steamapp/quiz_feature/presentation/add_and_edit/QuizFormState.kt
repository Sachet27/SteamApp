package com.example.steamapp.quiz_feature.presentation.add_and_edit

import com.example.steamapp.quiz_feature.data.local.entities.relations.QuizWithQuestions

data class QuizFormState(
    val quizWithQuestions: QuizWithQuestions?= null,
    val title: String= "",
    val optionA: String= "",
    val optionB: String= "",
    val optionC: String= "",
    val optionD: String= "",
    val correctOptionIndex: Int=0,
    val imageRelativePath: String?= null,
    val audioRelativePath: String?= null,
    val questionCount: Int=0
)

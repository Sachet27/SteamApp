package com.example.steamapp.api.presentation

import com.example.steamapp.quiz_feature.data.local.entities.relations.QuizWithQuestions

sealed class APIActions() {
    data class onPushToPi(val quizWithQuestions: QuizWithQuestions): APIActions()
    data object onCancelUpload: APIActions()
}
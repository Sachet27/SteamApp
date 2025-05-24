package com.example.steamapp.api.presentation

import com.example.steamapp.quiz_feature.data.local.entities.relations.QuizWithQuestions
import com.example.steamapp.quiz_feature.domain.models.Quiz

sealed class APIActions() {
    data class onPushToPi(val quizWithQuestions: QuizWithQuestions): APIActions()
    data object onCancelUpload: APIActions()
    data class onDownloadFromPi(val quiz: Quiz): APIActions()
    data object onCancelDownload: APIActions()
    data class onDeleteFromPi(val quizId: Long, val quizName: String): APIActions()
    data class onPresent(val quizId: Long): APIActions()
}
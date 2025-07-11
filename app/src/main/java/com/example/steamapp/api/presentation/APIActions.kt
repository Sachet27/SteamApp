package com.example.steamapp.api.presentation

import android.content.Context
import com.example.steamapp.api.domain.models.AnswerStyle
import com.example.steamapp.material_feature.domain.models.StudyMaterial
import com.example.steamapp.quiz_feature.data.local.entities.relations.QuizWithQuestions
import com.example.steamapp.quiz_feature.domain.models.Quiz

sealed class APIActions() {
    //upload download
    data class onDeleteMaterialFromPi(val material: StudyMaterial): APIActions()
    data class onPresentPdf(val material: StudyMaterial): APIActions()
    data class onPushMaterialToPi(val material: StudyMaterial): APIActions()
    data object onCancelMaterialUpload: APIActions()
    data class onDownloadMaterialFromPi(val material: StudyMaterial): APIActions()
    data object onCancelMaterialDownload: APIActions()

    data class onPushToPi(val quizWithQuestions: QuizWithQuestions): APIActions()
    data object onCancelUpload: APIActions()
    data class onDownloadFromPi(val quiz: Quiz): APIActions()
    data object onCancelDownload: APIActions()

    data class onDeleteFromPi(val quizId: Long, val quizName: String): APIActions()

    data class onPresent(val quizId: Long, val quizName: String): APIActions()
    data object onNext: APIActions()
    data object onPrevious: APIActions()
    data object onFinish: APIActions()
    data object onExit: APIActions()
    data object onPlayAudio: APIActions()
    data object onPauseAudio: APIActions()


    //ai model
    data class onPushMicAction(val micOn: Boolean, val context: Context): APIActions()
    data class onAskOllama(val userId: String, val question: String, val think: Boolean): APIActions()
    data class onSelectAnswerStyle(val userId: String, val answerStyle: AnswerStyle): APIActions()
    data object onClearAIQuestionState: APIActions()
}
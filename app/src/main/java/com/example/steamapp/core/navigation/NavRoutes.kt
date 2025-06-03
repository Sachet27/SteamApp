package com.example.steamapp.core.navigation

import kotlinx.serialization.Serializable

interface NavRoutes {
    @Serializable
    data object HomeRoute: NavRoutes
    @Serializable
    data object AddEditRoute: NavRoutes
    @Serializable
    data class DisplayRoute(val showAnswer: Boolean): NavRoutes
    @Serializable
    data class ScoreRoute(val questionCount: Int): NavRoutes


    @Serializable
    data object AskAIRoute: NavRoutes

    @Serializable
    data object LoginRoute: NavRoutes

    @Serializable
    data object MaterialHomeRoute: NavRoutes

    @Serializable
    data class PdfDisplayRoute(val notSyncWithPi: Boolean): NavRoutes

    //for students
    @Serializable
    data object StudentHomeRoute: NavRoutes

    @Serializable
    data object StudentMaterialRoute: NavRoutes

    @Serializable
    data object StudentQuizTestRoute: NavRoutes

    @Serializable
    data class StudentTestQuizScoreRoute( val score: Int, val total: Int): NavRoutes

    @Serializable
    data object DummyPdfDisplay: NavRoutes

    @Serializable
    data object StudentAskAIRoute: NavRoutes
}
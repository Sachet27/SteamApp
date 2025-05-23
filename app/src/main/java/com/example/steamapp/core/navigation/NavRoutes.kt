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
    data object AskAIRoute: NavRoutes
}
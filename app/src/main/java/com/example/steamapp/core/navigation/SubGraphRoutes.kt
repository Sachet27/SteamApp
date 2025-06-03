package com.example.steamapp.core.navigation

import kotlinx.serialization.Serializable

sealed interface SubGraph {
    @Serializable
    data object AuthRoute: SubGraph
    @Serializable
    data object QuizRoute: SubGraph
    @Serializable
    data object MaterialRoute: SubGraph

    @Serializable
    data object StudentRoute: SubGraph
}
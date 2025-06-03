package com.example.steamapp.api.domain.models

data class AIQuestion(
    val userId: String,
    val question:String,
    val think: Boolean
)

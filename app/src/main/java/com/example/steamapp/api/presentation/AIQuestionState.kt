package com.example.steamapp.api.presentation

import com.example.steamapp.api.domain.models.Intellect

data class AIQuestionState(
    val isLoading: Boolean= false,
    val question: String?= null,
    val answer: String? = null,
    val intellect: Intellect= Intellect.NORMAL
)
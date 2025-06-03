package com.example.steamapp.api.domain.models

enum class AnswerStyle {
    THINK, NO_THINK;
    companion object{
        val list= listOf(THINK, NO_THINK)
    }
}

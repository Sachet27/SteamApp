package com.example.steamapp.core.util

import com.example.steamapp.quiz_feature.data.local.entities.QuestionEntity
import com.example.steamapp.quiz_feature.presentation.add_and_edit.QuizFormState

fun List<QuestionEntity>.replace(targetIndex: Int, state: QuizFormState): List<QuestionEntity>{
    return this.mapIndexed { index, question ->
        if(index== targetIndex){
            question.copy(
                id = question.id,
                title = state.title,
                options = listOf(
                    state.optionA,
                    state.optionB,
                    state.optionC,
                    state.optionD
                ),
                correctOptionIndex = state.correctOptionIndex,
                imageRelativePath = state.imageRelativePath,
                audioRelativePath = state.audioRelativePath,
                quizId = question.quizId
            )
        } else{
            question
        }
    }
}
package com.example.steamapp.core.util

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.steamapp.quiz_feature.data.local.entities.QuestionEntity
import com.example.steamapp.quiz_feature.presentation.add_and_edit.MediaState
import com.example.steamapp.quiz_feature.presentation.add_and_edit.QuizFormState
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun List<QuestionEntity>.replace(targetIndex: Int, state: QuizFormState, mediaState: MediaState): List<QuestionEntity>{
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
                quizId = question.quizId,
                imageRelativePath = mediaState.imageRelativePath,
                audioRelativePath = mediaState.audioRelativePath
            )
        } else{
            question
        }
    }
}

fun String.formatQuizName(): String{
    return this.replace(" ", "_")
}

fun String.toRawQuizName():String{
    return this.replace("_", " ")
}

fun getRelativePath(quizPath: String): String{
    if(quizPath.contains("quizzes/")){
        val index= quizPath.indexOf("quizzes/")
        return quizPath.substring(index, quizPath.length)
    }
    else {
        return quizPath
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun Instant.formatToReadableDate(): String {
    val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy").withZone(ZoneId.systemDefault())
    return formatter.format(this)
}
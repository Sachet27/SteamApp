package com.example.steamapp.api.data.mappers

import com.example.steamapp.api.data.networking.dto.ControlModeDto
import com.example.steamapp.api.data.networking.dto.DisplayDto
import com.example.steamapp.api.domain.models.Action
import com.example.steamapp.api.domain.models.ControlMode
import com.example.steamapp.api.domain.models.Display
import com.example.steamapp.api.domain.models.DisplayMode

fun Display.toDisplayDto(): DisplayDto{
    return DisplayDto(
        quizName = quizName,
        displayMode = when(displayMode){
            DisplayMode.FULL_SCREEN-> "FULL_SCREEN"
        }
    )
}

fun ControlMode.toControlModeDto(): ControlModeDto{
    return ControlModeDto(
        action = when(action){
            Action.NEXT-> "NEXT"
            Action.PREVIOUS -> "PREVIOUS"
            Action.EXIT -> "EXIT"
            Action.FINISH -> "FINISH"
            Action.PLAY_AUDIO -> "PLAY_AUDIO"
            Action.PAUSE_AUDIO -> "PAUSE_AUDIO"
        }
    )
}
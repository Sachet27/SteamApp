package com.example.steamapp.auth.domain.models

enum class Role {
    STUDENT, TEACHER
}



fun Role.toString(): String {
    return when(this){
        Role.STUDENT -> "STUDENT"
        Role.TEACHER -> "TEACHER"
    }
}

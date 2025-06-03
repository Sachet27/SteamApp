package com.example.steamapp.auth.domain.models

data class User(
    val userId: String,
    val password: String,
    val role: Role
)

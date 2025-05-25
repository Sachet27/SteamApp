package com.example.steamapp.auth.data.remote.dtos

import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val user_id: String,
    val password: String
)

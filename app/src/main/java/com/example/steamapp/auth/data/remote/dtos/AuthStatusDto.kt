package com.example.steamapp.auth.data.remote.dtos

import kotlinx.serialization.Serializable

@Serializable
data class AuthStatusDto(
    val status: Boolean
)

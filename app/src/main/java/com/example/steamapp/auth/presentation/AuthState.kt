package com.example.steamapp.auth.presentation

import com.example.steamapp.auth.domain.models.Role

data class AuthState(
    val isSignedIn: AuthResponse = AuthResponse.Unauthenticated,
    val userId: String= "",
    val password: String = "",
    val userRole: Role= Role.STUDENT,
    val isLoading: Boolean= false
)
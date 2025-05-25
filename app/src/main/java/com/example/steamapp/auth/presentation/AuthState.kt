package com.example.steamapp.auth.presentation

data class AuthState(
    val isSignedIn: AuthResponse = AuthResponse.Unauthenticated,
    val userId: String= "",
    val password: String = "",
    val isLoading: Boolean= false
)
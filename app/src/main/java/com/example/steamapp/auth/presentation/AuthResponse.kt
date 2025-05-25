package com.example.steamapp.auth.presentation

sealed interface AuthResponse{
    data object Authenticated: AuthResponse
    data object Unauthenticated: AuthResponse
    data class Error( val message: String): AuthResponse
}
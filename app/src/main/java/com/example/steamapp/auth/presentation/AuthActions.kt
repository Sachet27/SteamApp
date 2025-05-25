package com.example.steamapp.auth.presentation

import android.content.Context

sealed class AuthActions(){
        data class OnUserIdChange(val newUserId: String): AuthActions()
        data class OnPasswordChange(val newPassword: String): AuthActions()
        data class OnSignInWithUserIdAndPassword(val context: Context): AuthActions()
        data object OnSignOut: AuthActions()
}

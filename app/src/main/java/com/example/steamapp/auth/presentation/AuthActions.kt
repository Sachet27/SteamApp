package com.example.steamapp.auth.presentation

import android.content.Context
import com.example.steamapp.auth.domain.models.Role

sealed class AuthActions(){
        data class OnUserIdChange(val newUserId: String): AuthActions()
        data class OnChangeUserRole(val newRole: Role): AuthActions()
        data class OnPasswordChange(val newPassword: String): AuthActions()
        data class OnSignInWithUserIdAndPassword(val context: Context): AuthActions()
        data object OnSignOut: AuthActions()
}

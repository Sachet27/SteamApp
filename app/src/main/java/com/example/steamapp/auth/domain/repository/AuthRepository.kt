package com.example.steamapp.auth.domain.repository

import com.example.steamapp.auth.data.remote.dtos.AuthStatusDto
import com.example.steamapp.auth.domain.models.User
import com.example.steamapp.core.util.networking.NetworkError
import com.example.steamapp.core.util.networking.Result

interface AuthRepository {
    suspend fun signInWithUserIdAndPassword(user: User): Result<AuthStatusDto, NetworkError>
    suspend fun signOut()
}
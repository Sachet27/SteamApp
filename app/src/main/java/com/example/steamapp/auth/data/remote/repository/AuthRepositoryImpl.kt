package com.example.steamapp.auth.data.remote.repository

import com.example.steamapp.auth.data.remote.dtos.AuthStatusDto
import com.example.steamapp.auth.data.remote.dtos.UserDto
import com.example.steamapp.auth.data.remote.mappers.toUserDto
import com.example.steamapp.auth.domain.models.User
import com.example.steamapp.auth.domain.repository.AuthRepository
import com.example.steamapp.core.data.networking.constructQuizUrl
import com.example.steamapp.core.data.networking.safeCall
import com.example.steamapp.core.util.networking.NetworkError
import com.example.steamapp.core.util.networking.Result
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class AuthRepositoryImpl(
    private val httpClient: HttpClient
): AuthRepository {
    override suspend fun signInWithUserIdAndPassword(user: User): Result<AuthStatusDto, NetworkError> {
        return safeCall<AuthStatusDto> {
            httpClient.post(
                urlString = constructQuizUrl("/login")
            ){
                contentType(ContentType.Application.Json)
                setBody(user.toUserDto())
            }
        }
    }

    override suspend fun signOut() {
        //will add if custom sign out functionality is added
    }

}
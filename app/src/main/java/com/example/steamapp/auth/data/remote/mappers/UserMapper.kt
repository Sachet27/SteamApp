package com.example.steamapp.auth.data.remote.mappers

import com.example.steamapp.auth.data.remote.dtos.UserDto
import com.example.steamapp.auth.domain.models.User

fun UserDto.toUser(): User{
    return User(
        userId = user_id,
        password = password
    )
}

fun User.toUserDto(): UserDto {
    return UserDto(
        user_id= userId,
        password = password
    )
}
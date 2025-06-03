package com.example.steamapp.auth.data.remote.mappers

import com.example.steamapp.auth.data.remote.dtos.UserDto
import com.example.steamapp.auth.domain.models.Role
import com.example.steamapp.auth.domain.models.User

fun UserDto.toUser(): User{
    return User(
        userId = user_id,
        password = password,
        role = when(role){
            "STUDENT" -> Role.STUDENT
            "TEACHER"-> Role.TEACHER
            else-> Role.STUDENT
        }
    )
}

fun User.toUserDto(): UserDto {
    return UserDto(
        user_id= userId,
        password = password,
        role = role.toString()
    )
}
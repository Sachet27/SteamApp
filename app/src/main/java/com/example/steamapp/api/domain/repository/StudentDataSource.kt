package com.example.steamapp.api.domain.repository

import com.example.steamapp.core.util.networking.NetworkError
import com.example.steamapp.core.util.networking.Result
import com.example.steamapp.student.StudentDetail

interface StudentDataSource {
    suspend fun getStudentReport(name: String): Result<StudentDetail, NetworkError>
}
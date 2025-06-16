package com.example.steamapp.core.presentation.student

import com.example.steamapp.student.StudentDetail

data class StudentListState(
    val isLoading: Boolean = false,
    val studentList: List<String> = emptyList(),
    val selectedStudentReport: StudentDetail? = null
)

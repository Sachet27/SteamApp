package com.example.steamapp.student.quiz.presentation

import com.example.steamapp.material_feature.domain.models.StudyMaterial

sealed interface StudentQuizActions {
    data object onClearSelectedMaterial: StudentQuizActions
    data class onLoadQuizForTest(val id: Long): StudentQuizActions
    data object onClearSelectedQuiz: StudentQuizActions
    data class onLoadPdfMaterial(val material: StudyMaterial): StudentQuizActions
}

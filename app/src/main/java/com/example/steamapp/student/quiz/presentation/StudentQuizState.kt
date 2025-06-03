package com.example.steamapp.student.quiz.presentation

import com.example.steamapp.material_feature.domain.models.StudyMaterial
import com.example.steamapp.quiz_feature.data.local.entities.relations.QuizWithQuestions

data class StudentQuizState(
    val isLoading: Boolean= false,
    val quizzes: List<QuizWithQuestions> = emptyList(),
    val materials: List<StudyMaterial> = emptyList(),
    val selectedQuiz: QuizWithQuestions?= null,
    val selectedMaterial: StudyMaterial?=null
)

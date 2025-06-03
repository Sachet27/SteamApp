package com.example.steamapp.di

import com.example.steamapp.core.navigation.SubGraph
import com.example.steamapp.student.quiz.presentation.StudentViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val studentModule= module {
    scope<SubGraph.StudentRoute> {
        viewModel { StudentViewModel(get()) }
    }
}
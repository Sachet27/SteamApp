package com.example.steamapp.di

import com.example.steamapp.material_feature.data.repository.MaterialRepositoryImpl
import com.example.steamapp.material_feature.domain.repository.MaterialRepository
import com.example.steamapp.quiz_feature.data.local.database.QuizDatabase
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import com.example.steamapp.material_feature.presentation.MaterialViewModel

val materialModule= module {
    single<MaterialRepository>{
        MaterialRepositoryImpl(
            dao = get<QuizDatabase>().materialDao
        )
    }
    viewModelOf(::MaterialViewModel)
}
package com.example.steamapp.di

import androidx.room.Room
import com.example.steamapp.api.data.networking.repository.APIRepositoryImpl
import com.example.steamapp.api.domain.repository.APIRepository
import com.example.steamapp.core.data.internal_storage.FileManager
import com.example.steamapp.core.data.networking.HttpClientFactory
import com.example.steamapp.quiz_feature.data.local.database.QuizDatabase
import com.example.steamapp.quiz_feature.data.repository.QuizRepositoryImpl
import com.example.steamapp.quiz_feature.domain.repository.QuizRepository
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import com.example.steamapp.quiz_feature.presentation.QuizViewModel
import com.example.steamapp.api.presentation.APIViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO

val quizModule= module {
    single<QuizRepository> {
        QuizRepositoryImpl(dao = get<QuizDatabase>().dao)
    }
    single {
        Room.databaseBuilder(
            androidContext(),
            QuizDatabase::class.java,
            QuizDatabase.DB_NAME
        ).build()
    }
    single<FileManager>{
        FileManager(context = androidContext())
    }
    single<HttpClient> {
        HttpClientFactory.create(CIO.create())
    }
    single<APIRepository>{
        APIRepositoryImpl(httpClient = get(), fileManager = get())
    }
    viewModelOf(::APIViewModel)
    viewModelOf(::QuizViewModel)
}
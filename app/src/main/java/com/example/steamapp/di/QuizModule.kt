package com.example.steamapp.di

import androidx.room.Room
import com.example.steamapp.quiz_feature.data.local.database.QuizDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val quizModule= module {
    single {
        Room.databaseBuilder(
            androidContext(),
            QuizDatabase::class.java,
            QuizDatabase.DB_NAME
        ).build()
    }
}
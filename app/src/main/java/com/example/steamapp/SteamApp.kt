package com.example.steamapp

import android.app.Application
import com.example.steamapp.di.quizModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin


class SteamApp: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin{
            androidContext(this@SteamApp)
            androidLogger()
            modules(quizModule)
        }
    }
}
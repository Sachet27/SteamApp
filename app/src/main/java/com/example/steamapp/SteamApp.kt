package com.example.steamapp

import android.app.Application
import com.example.steamapp.di.quizModule
import io.ktor.client.plugins.logging.LogLevel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level


class SteamApp: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin{
            androidContext(this@SteamApp)
            androidLogger(Level.ERROR)
            modules(quizModule)
        }
    }
}
package com.example.steamapp.di

import com.example.steamapp.auth.data.datastore.createDataStore
import com.example.steamapp.auth.presentation.AuthViewModel
import com.example.steamapp.auth.data.remote.repository.AuthRepositoryImpl
import com.example.steamapp.auth.domain.repository.AuthRepository
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val authModule= module {
    single{ createDataStore(androidContext()) }
    single<AuthRepository>{
        AuthRepositoryImpl(httpClient = get())
    }
    viewModelOf(::AuthViewModel)
}
package com.example.steamapp.core.data.networking

import com.example.steamapp.BuildConfig

fun constructQuizUrl(url:String): String {
    return when{
        url.contains(BuildConfig.QUIZ_API_BASE_URL) -> url
        url.startsWith("/") -> BuildConfig.QUIZ_API_BASE_URL + url.drop(1)
        else-> BuildConfig.QUIZ_API_BASE_URL+ url
    }
}

fun constructAIUrl(url:String): String {
    return when{
        url.contains(BuildConfig.AI_API_BASE_URL) -> url
        url.startsWith("/") -> BuildConfig.AI_API_BASE_URL + url.drop(1)
        else-> BuildConfig.AI_API_BASE_URL+ url
    }
}
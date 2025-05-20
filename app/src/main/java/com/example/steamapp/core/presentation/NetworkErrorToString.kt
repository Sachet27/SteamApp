package com.example.steamapp.core.presentation

import android.content.Context
import com.example.steamapp.R
import com.example.steamapp.core.util.networking.NetworkError

fun NetworkError.toString(context: Context): String{
    val resId= when(this){
        NetworkError.REQUEST_TIMEOUT -> R.string.error_request_timeout
        NetworkError.TOO_MANY_REQUESTS -> R.string.too_many_requests
        NetworkError.NO_INTERNET -> R.string.no_internet
        NetworkError.SERVER_ERROR -> R.string.server_error
        NetworkError.SERIALIZATION -> R.string.serialization
        NetworkError.UNKNOWN -> R.string.server_error
    }
    return context.getString(resId)
}
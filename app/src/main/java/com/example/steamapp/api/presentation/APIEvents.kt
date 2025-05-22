package com.example.steamapp.api.presentation

import com.example.steamapp.core.util.networking.NetworkError

sealed interface APIEvents {
    data class Error(val error: NetworkError): APIEvents
}
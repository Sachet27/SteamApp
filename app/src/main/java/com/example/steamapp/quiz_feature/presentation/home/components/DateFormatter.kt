package com.example.steamapp.quiz_feature.presentation.home.components

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
fun Instant.toDateString(): String{
    val formatter= DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val localDateTime= this.atZone(ZoneId.systemDefault()).toLocalDateTime()
    return formatter.format(localDateTime)
}
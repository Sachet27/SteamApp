package com.example.steamapp.quiz_feature.data.local.converters

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import java.time.Instant

class Converters {
    @TypeConverter
    fun fromStringList(value: List<String>): String{
        return value.joinToString(",")
    }

    @TypeConverter
    fun toStringList(value: String): List<String>{
        return value.split(",").map { it.trim() }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun fromInstant(value: Instant): Long{
        return value.toEpochMilli()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun toInstant(value: Long): Instant{
        return Instant.ofEpochMilli(value)
    }
}
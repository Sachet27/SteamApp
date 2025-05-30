package com.example.steamapp.quiz_feature.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.steamapp.material_feature.data.local.dao.MaterialDao
import com.example.steamapp.material_feature.data.local.entities.StudyMaterialEntity
import com.example.steamapp.quiz_feature.data.local.converters.Converters
import com.example.steamapp.quiz_feature.data.local.dao.QuizDao
import com.example.steamapp.quiz_feature.data.local.entities.QuestionEntity
import com.example.steamapp.quiz_feature.data.local.entities.QuizEntity

@Database(
    entities = [QuizEntity::class, QuestionEntity::class, StudyMaterialEntity::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class QuizDatabase: RoomDatabase(){
    abstract val quizDao: QuizDao
    abstract val materialDao: MaterialDao

    companion object{
        const val DB_NAME= "quiz_db"
    }
}
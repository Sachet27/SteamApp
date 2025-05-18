package com.example.steamapp.quiz_feature.presentation.add_and_edit

data class MediaState(
    val audioRelativePath: String?= null,
    val imageRelativePath: String?= null,
    val isUploading: Boolean= false,
)

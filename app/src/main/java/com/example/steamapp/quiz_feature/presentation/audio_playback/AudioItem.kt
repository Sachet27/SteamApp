package com.example.steamapp.quiz_feature.presentation.audio_playback

import android.net.Uri
import androidx.media3.common.MediaItem

data class AudioItem (
    val uri: Uri,
    val mediaItem: MediaItem,
    val name: String
)
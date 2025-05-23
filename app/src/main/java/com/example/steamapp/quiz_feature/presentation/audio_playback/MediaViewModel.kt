package com.example.steamapp.quiz_feature.presentation.audio_playback

import android.net.Uri
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class MediaViewModel (
    val player: Player,
    private val metaDataReader: MetaDataReader
): ViewModel() {

    private val audioUri= MutableStateFlow<Uri?>(null)

    val audioItem = audioUri.map { uri ->
        uri?.let{
            AudioItem(
                uri = uri,
                mediaItem = MediaItem.fromUri(uri),
                name = metaDataReader.getMetaDataFromUri(uri)?.fileName ?: "No name"
            )
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    init {
        player.prepare()
    }

    fun setAudioUri(uri: Uri) {
        player.setMediaItem(MediaItem.fromUri(uri))
    }

    override fun onCleared() {
        super.onCleared()
        player.release()
    }
}
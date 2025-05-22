package com.example.steamapp.api.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.steamapp.api.data.mappers.toUploadResponse
import com.example.steamapp.api.domain.models.UploadResponse
import com.example.steamapp.api.domain.repository.APIRepository
import com.example.steamapp.core.util.networking.UploadStatus
import com.example.steamapp.core.util.networking.map
import com.example.steamapp.core.util.networking.onError
import com.example.steamapp.core.util.networking.onSuccess
import com.example.steamapp.quiz_feature.data.local.entities.relations.QuizWithQuestions
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.io.FileNotFoundException

class APIViewModel (
    private val apiRepository: APIRepository
): ViewModel(){
    private val _uploadState= MutableStateFlow(UploadState())
    val uploadState= _uploadState.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        UploadState()
    )
    private var uploadJob: Job?= null

    private val _events= Channel<APIEvents>()
    val events= _events.receiveAsFlow()

    fun onAction(action: APIActions){
        when(action){
            is APIActions.onPushToPi-> { pushQuizWithQuestions(action.quizWithQuestions)}
            is APIActions.onCancelUpload-> {cancelUpload()}
        }
    }

     private fun pushQuizWithQuestions(quizWithQuestions: QuizWithQuestions){
         Log.d("Yeet", "entered upload Function")
        uploadJob= apiRepository
            .pushQuizWithQuestions(quizWithQuestions)
            .onStart {
                _uploadState.update {
                    it.copy(
                        isUploading = true,
                        isUploadComplete = false,
                        error= null,
                        progress = 0f,
                        uploadResponse = UploadResponse()
                    )
                }
            }
            .onEach { uploadStatus ->
                when (uploadStatus) {
                    is UploadStatus.Progress -> {
                        _uploadState.update {
                            it.copy(
                                progress = uploadStatus.progress.bytesSent / uploadStatus.progress.totalBytes.toFloat()
                            )
                        }
                    }
                    is UploadStatus.ResultState -> {
                        uploadStatus.result
                            .onSuccess {response->
                                _uploadState.update {
                                    it.copy(
                                        uploadResponse = response.toUploadResponse()
                                    )
                                }
                            }
                            .onError { error->
                                 _events.send(APIEvents.Error(error))
                            }
                    }
                }
            }
            .onCompletion { cause->
                if(cause==null){
                    _uploadState.update {
                        it.copy(
                            isUploading = false,
                            isUploadComplete = true
                        )
                    }
                    //delete the quiz locally afterwards through events
                } else if(cause is CancellationException){
                    _uploadState.update {
                        it.copy(
                            isUploading = false,
                            isUploadComplete = false,
                            error = "The upload was cancelled.",
                            progress = 0f
                        )
                    }
                }
            } .catch {cause ->  
                val message= when(cause){
                    is OutOfMemoryError-> "Memory insufficient!"
                    is FileNotFoundException-> "File not found!"
                    is UnresolvedAddressException-> "No internet!"
                    else -> "Something went wrong!"
                }
                _uploadState.update {
                    it.copy(
                        isUploading = false,
                        error = message
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private fun cancelUpload(){
        uploadJob?.cancel()
    }

}

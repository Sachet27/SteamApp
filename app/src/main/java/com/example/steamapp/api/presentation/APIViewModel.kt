package com.example.steamapp.api.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.steamapp.api.data.mappers.toUploadResponse
import com.example.steamapp.api.domain.models.AIQuestion
import com.example.steamapp.api.domain.models.Intellect
import com.example.steamapp.api.domain.models.IntellectRequest
import com.example.steamapp.api.domain.models.UploadResponse
import com.example.steamapp.api.domain.repository.APIRepository
import com.example.steamapp.api.presentation.components.DownloadState
import com.example.steamapp.core.util.networking.DownloadStatus
import com.example.steamapp.core.util.networking.NetworkError
import com.example.steamapp.core.util.networking.UploadStatus
import com.example.steamapp.core.util.networking.onError
import com.example.steamapp.core.util.networking.onSuccess
import com.example.steamapp.quiz_feature.data.local.entities.QuizEntity
import com.example.steamapp.quiz_feature.data.local.entities.relations.QuizWithQuestions
import com.example.steamapp.quiz_feature.domain.mappers.toQuizEntity
import com.example.steamapp.quiz_feature.domain.models.Quiz
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
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
import kotlinx.coroutines.launch
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

    private val _downloadState= MutableStateFlow(DownloadState())
    val downloadState= _downloadState.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        DownloadState()
    )
    private var downloadJob: Job? = null

    private val _aiQuestionState= MutableStateFlow(AIQuestionState())
    val aiQuestionState= _aiQuestionState
        .stateIn(
        viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            AIQuestionState()
    )


    private val _events= Channel<APIEvents>()
    val events= _events.receiveAsFlow()

    fun onAction(action: APIActions){
        when(action){
            is APIActions.onPushToPi-> { pushQuizWithQuestions(action.quizWithQuestions)}
            is APIActions.onCancelUpload-> {cancelUpload()}
            is APIActions.onCancelDownload -> { cancelDownload()}
            is APIActions.onDownloadFromPi -> {getQuizWithQuestions(quiz = action.quiz.toQuizEntity())}
            is APIActions.onDeleteFromPi -> {deletePiQuiz(action.quizId, action.quizName)}
            is APIActions.onPresent -> {

            }

            is APIActions.onAskOllama -> {askOllamaAI(action.userId, action.question)}
            is APIActions.onSelectIntellectLevel -> {selectIntellectLevel(action.userId, action.intellect)}
            is APIActions.onClearAIQuestionState -> {clearAIState()}
        }
    }


     private fun pushQuizWithQuestions(quizWithQuestions: QuizWithQuestions){
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

    private fun getQuizWithQuestions(quiz: QuizEntity){
        downloadJob= apiRepository
            .getQuizWithQuestions(quizId = quiz.quizId, rawQuizName = quiz.title)
            .onStart {
                _downloadState.update {
                    it.copy(
                        isUploading = true,
                        isUploadComplete = false,
                        error= null,
                        progress = 0f,
                        )
                }
            }
            .onEach {downloadStatus->
                when(downloadStatus){
                    is DownloadStatus.Progress -> {
                        _downloadState.update {
                            it.copy(
                                progress = downloadStatus.progress.bytesSent/downloadStatus.progress.totalBytes.toFloat()
                            )
                        }
                    }
                    is DownloadStatus.ResultState -> {
                        downloadStatus.result
                            .onSuccess {
                                Log.d("Yeet","Viewmodel: Download process finished")
                            }
                            .onError {error->
                                _events.send(APIEvents.Error(error))
                            }
                    }
                }
            }
            .onCompletion {cause->
                Log.d("Yeet", "on Completition triggered")
                if(cause==null){
                    _downloadState.update {
                        it.copy(
                            isUploading = false,
                            isUploadComplete = true
                        )
                    }
                } else if(cause is CancellationException){
                    _downloadState.update {
                        it.copy(
                            isUploading = false,
                            isUploadComplete = false,
                            error = "The download was cancelled.",
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
                _downloadState.update {
                    it.copy(
                        isUploading = false,
                        error = message
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private fun cancelDownload(){
        downloadJob?.cancel()
    }

    private fun deletePiQuiz(quizId: Long, quizName: String){
        viewModelScope.launch(Dispatchers.IO) {
            apiRepository.deleteQuizByQuizId(quizId, quizName)
        }
    }

    private fun selectIntellectLevel(userId: String, intellect: Intellect ){
        _aiQuestionState.update {
            it.copy(isLoading = true)
        }
        viewModelScope.launch(Dispatchers.IO) {
            apiRepository.selectIntellectLevel(
                intellect = IntellectRequest(intellect),
                user_id = userId
            )
                .onSuccess {response->
                    _aiQuestionState.update {
                        it.copy(isLoading = false, intellect = response.intellect)
                    }
                }
                .onError {
                    _events.send(APIEvents.Error(it))
                    _aiQuestionState.update {
                        it.copy(isLoading = false)
                    }
                }
        }
    }

    private fun askOllamaAI(userId: String, question: String){
        _aiQuestionState.update {
            it.copy(isLoading = true, question = question)
        }
        viewModelScope.launch(Dispatchers.IO) {
            apiRepository.askQuestion(
                AIQuestion(
                    userId = userId,
                    question = question
                )
            )
                .onSuccess {answer->
                    _aiQuestionState.update {
                        it.copy(isLoading = false, question = answer.question, answer = answer.answer)
                    }
                }
                .onError {
                    _events.send(APIEvents.Error(it))
                    _aiQuestionState.update {
                        it.copy(question = null, isLoading= false)
                    }
                }
        }
    }

    private fun clearAIState(){
        _aiQuestionState.update {
            it.copy(isLoading = false, question = null, answer = null)
        }
    }

}

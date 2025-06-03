package com.example.steamapp.api.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.steamapp.api.data.mappers.toUploadResponse
import com.example.steamapp.api.domain.models.AIQuestion
import com.example.steamapp.api.domain.models.Action
import com.example.steamapp.api.domain.models.AnswerStyle
import com.example.steamapp.api.domain.models.AnswerStyleRequest
import com.example.steamapp.api.domain.models.ControlMode
import com.example.steamapp.api.domain.models.Display
import com.example.steamapp.api.domain.models.UploadResponse
import com.example.steamapp.api.domain.repository.APIRepository
import com.example.steamapp.api.presentation.components.DownloadState
import com.example.steamapp.core.util.formatQuizName
import com.example.steamapp.core.util.networking.DownloadStatus
import com.example.steamapp.core.util.networking.UploadStatus
import com.example.steamapp.core.util.networking.onError
import com.example.steamapp.core.util.networking.onSuccess
import com.example.steamapp.material_feature.domain.models.StudyMaterial
import com.example.steamapp.quiz_feature.data.local.entities.QuizEntity
import com.example.steamapp.quiz_feature.data.local.entities.relations.QuizWithQuestions
import com.example.steamapp.quiz_feature.domain.mappers.toQuizEntity
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
    private val _materialUploadState= MutableStateFlow(UploadState())
    val materialUploadState= _materialUploadState.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        UploadState()
    )
    private var materialUploadJob: Job?= null

    private val _materialDownloadState= MutableStateFlow(DownloadState())
    val materialDownloadState= _materialDownloadState.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        DownloadState()
    )
    private var materialDownloadJob: Job? = null

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
                presentQuizOnPi(action.quizId, action.quizName)
            }

            is APIActions.onAskOllama -> {askOllamaAI(action.userId, action.question, action.think)}
            is APIActions.onSelectAnswerStyle -> {
            }
            is APIActions.onClearAIQuestionState -> {clearAIState()}
            APIActions.onExit -> {
                exitDisplay()
            }
            APIActions.onFinish -> {
                finishDisplay()
            }
            APIActions.onNext -> {moveToNextQuestion()}
            APIActions.onPrevious -> {moveToPreviousQuestion()}
            APIActions.onPauseAudio -> {pauseAudio()}
            APIActions.onPlayAudio -> {
                playAudio()
            }

            APIActions.onCancelMaterialDownload -> {
                cancelMaterialDownload()
            }
            APIActions.onCancelMaterialUpload -> {
                cancelMaterialUpload()
            }
            is APIActions.onDownloadMaterialFromPi -> {getMaterial(action.material)}
            is APIActions.onPushMaterialToPi -> { pushMaterial(action.material)}
            is APIActions.onPresentPdf -> {}
            is APIActions.onDeleteMaterialFromPi -> {deletePiMaterial(action.material)}
        }
    }


    private fun deletePiMaterial(material: StudyMaterial){
        viewModelScope.launch(Dispatchers.IO) {
            apiRepository.deleteMaterialById(
                id = material.id,
                name = material.name
            )
        }
    }

    private fun playAudio(){
        viewModelScope.launch {
            apiRepository.pushAction(
                controlMode = ControlMode(Action.PLAY_AUDIO)
            )
                .onSuccess {
                    Log.d("Yeet", "Playing audio")
                }
                .onError {
                    _events.send(APIEvents.Error(it))
                }
        }
    }

    private fun pauseAudio(){
        viewModelScope.launch {
            apiRepository.pushAction(
                controlMode = ControlMode(Action.PAUSE_AUDIO)
            )
                .onSuccess {
                    Log.d("Yeet", "Paused audio")
                }
                .onError {
                    _events.send(APIEvents.Error(it))
                }
        }
    }

    private fun exitDisplay(){
        viewModelScope.launch {
            apiRepository.pushAction(
                controlMode = ControlMode(Action.EXIT)
            )
                .onSuccess {
                    Log.d("Yeet", "Successfully exited display")
                }
                .onError {
                    _events.send(APIEvents.Error(it))
                }
        }
    }

    private fun finishDisplay(){
        viewModelScope.launch {
            apiRepository.pushAction(
                controlMode = ControlMode(Action.FINISH)
            )
                .onSuccess {
                    Log.d("Yeet", "Successfully exited display")
                }
                .onError {
                    _events.send(APIEvents.Error(it))
                }
        }
    }

    private fun moveToNextQuestion(){
        viewModelScope.launch {
            apiRepository.pushAction(
                controlMode = ControlMode(Action.NEXT)
            )
                .onSuccess {
                    Log.d("Yeet", "Successfully move to next question")
                }
                .onError {
                    _events.send(APIEvents.Error(it))
                }
        }
    }

    private fun moveToPreviousQuestion(){
        viewModelScope.launch {
            apiRepository.pushAction(
                controlMode = ControlMode(Action.PREVIOUS)
            )
                .onSuccess {
                    Log.d("Yeet", "Successfully move to previous question")
                }
                .onError {
                    _events.send(APIEvents.Error(it))
                }
        }
    }

    private fun presentQuizOnPi(quizId: Long, quizName: String){
        val fileName= "$quizId-${quizName.formatQuizName()}"
        viewModelScope.launch {
            apiRepository.pushDisplay(
                Display(quizName = fileName)
            )
                .onSuccess {
                    Log.d("Yeet", "$fileName Displayed on Pi")
                }
                .onError {
                    _events.send(APIEvents.Error(it))
                }
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


    private fun getMaterial(material: StudyMaterial){
        downloadJob= apiRepository
            .getMaterial(
                id = material.id,
                name = material.name
            )
            .onStart {
                _materialDownloadState.update {
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
                        _materialDownloadState.update {
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
                    _materialDownloadState.update {
                        it.copy(
                            isUploading = false,
                            isUploadComplete = true
                        )
                    }
                } else if(cause is CancellationException){
                    _materialDownloadState.update {
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
                _materialDownloadState.update {
                    it.copy(
                        isUploading = false,
                        error = message
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private fun cancelMaterialDownload(){
        materialDownloadJob?.cancel()
    }


    private fun pushMaterial(material: StudyMaterial){
        uploadJob= apiRepository
            .pushMaterial(material)
            .onStart {
                _materialUploadState.update {
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
                        _materialUploadState.update {
                            it.copy(
                                progress = uploadStatus.progress.bytesSent / uploadStatus.progress.totalBytes.toFloat()
                            )
                        }
                    }
                    is UploadStatus.ResultState -> {
                        uploadStatus.result
                            .onSuccess {response->
                                Log.d("Yeet", "Result: Success")
                            }
                            .onError { error->
                                _events.send(APIEvents.Error(error))
                                Log.d("Yeet", error.toString())
                            }
                    }
                }
            }
            .onCompletion { cause->
                if(cause==null){
                    _materialUploadState.update {
                        it.copy(
                            isUploading = false,
                            isUploadComplete = true
                        )
                    }
                } else if(cause is CancellationException){
                    _materialUploadState.update {
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
                _materialUploadState.update {
                    it.copy(
                        isUploading = false,
                        error = message
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private fun cancelMaterialUpload(){
        materialUploadJob?.cancel()
    }


    private fun deletePiQuiz(quizId: Long, quizName: String){
        viewModelScope.launch(Dispatchers.IO) {
            apiRepository.deleteQuizByQuizId(quizId, quizName)
        }
    }


    private fun askOllamaAI(userId: String, question: String, think: Boolean){
        _aiQuestionState.update {
            it.copy(isLoading = true, question = question)
        }
        viewModelScope.launch(Dispatchers.IO) {
            apiRepository.askQuestion(
                AIQuestion(
                    userId = userId,
                    question = question,
                    think = think
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
            it.copy(isLoading = false, question = null, answer = null, think = false)
        }
    }

}

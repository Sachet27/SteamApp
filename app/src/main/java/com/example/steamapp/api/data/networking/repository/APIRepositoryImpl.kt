package com.example.steamapp.api.data.networking.repository

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.steamapp.api.data.mappers.toAIAnswer
import com.example.steamapp.api.data.mappers.toControlModeDto
import com.example.steamapp.api.data.mappers.toDisplayDto
import com.example.steamapp.api.data.mappers.toIntellectResponse
import com.example.steamapp.api.data.mappers.toQuizEntity
import com.example.steamapp.api.data.mappers.toQuizWithQuestions
import com.example.steamapp.api.data.networking.dto.AIAnswerDto
import com.example.steamapp.api.data.networking.dto.IntellectResponseDto
import com.example.steamapp.api.data.networking.dto.QuizDto
import com.example.steamapp.api.data.networking.dto.QuizListDto
import com.example.steamapp.api.data.networking.dto.QuizWithQuestionsDto
import com.example.steamapp.api.data.networking.models.ProgressUpdate
import com.example.steamapp.api.domain.models.ControlMode
import com.example.steamapp.api.data.networking.dto.UploadResponseDto
import com.example.steamapp.api.domain.models.AIAnswer
import com.example.steamapp.api.domain.models.AIQuestion
import com.example.steamapp.api.domain.models.Display
import com.example.steamapp.api.domain.models.FileInfo
import com.example.steamapp.api.domain.models.IntellectRequest
import com.example.steamapp.api.domain.models.IntellectResponse
import com.example.steamapp.api.domain.repository.APIRepository
import com.example.steamapp.core.data.internal_storage.FileManager
import com.example.steamapp.core.data.networking.constructAIUrl
import com.example.steamapp.core.data.networking.constructQuizUrl
import com.example.steamapp.core.data.networking.safeCall
import com.example.steamapp.core.util.formatQuizName
import com.example.steamapp.core.util.networking.DownloadStatus
import com.example.steamapp.core.util.networking.EmptyResult
import com.example.steamapp.core.util.networking.NetworkError
import com.example.steamapp.core.util.networking.Result
import com.example.steamapp.core.util.networking.UploadStatus
import com.example.steamapp.core.util.networking.map
import com.example.steamapp.core.util.networking.onSuccess
import com.example.steamapp.quiz_feature.data.local.entities.relations.QuizWithQuestions
import com.example.steamapp.quiz_feature.domain.mappers.toQuiz
import com.example.steamapp.quiz_feature.domain.models.Quiz
import io.ktor.client.HttpClient
import io.ktor.client.plugins.onDownload
import io.ktor.client.plugins.onUpload
import io.ktor.client.request.delete
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.readBytes
import io.ktor.client.statement.readRawBytes
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow

class APIRepositoryImpl(
    private val httpClient: HttpClient,
    private val fileManager: FileManager
): APIRepository() {

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getQuizzes(): Result<List<Quiz>, NetworkError> {
        return safeCall<List<QuizDto>> {
            httpClient.get(
                urlString = constructQuizUrl("/quizzes")
            )
        }.map { quizList->
            quizList.map {
                it.toQuizEntity().toQuiz()
            }
        }
    }

    override suspend fun deleteQuizByQuizId(quizId: Long, quizName:String): EmptyResult<NetworkError> {
        val fileName= "${quizId}-${quizName.formatQuizName()}"
        return safeCall {
            httpClient.delete(
            urlString = constructQuizUrl("/quiz-delete/${fileName}")
            )
        }
    }

    override suspend fun askQuestion(question: AIQuestion): Result<AIAnswer, NetworkError> {
        return safeCall<AIAnswerDto> {
            httpClient.post(
                urlString = constructAIUrl("/ask")
            )
        }.map {
            it.toAIAnswer()
        }
    }

    override suspend fun selectIntellectLevel(intellect: IntellectRequest, user_id: String): Result<IntellectResponse, NetworkError> {
        return safeCall<IntellectResponseDto> {
            httpClient.put(
                urlString = constructAIUrl("/user/$user_id")
            )
        }.map {
            it.toIntellectResponse()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getQuizWithQuestions(quizId:Long, rawQuizName: String): Flow<DownloadStatus<Unit, NetworkError>> = channelFlow {
        val downloadResponse= safeCall<Unit> {
           val httpResponse= httpClient.get(
                urlString = constructQuizUrl("/chapter/$quizId-${rawQuizName.formatQuizName()}")
            ){
                onDownload { bytesSentTotal, totalBytes->
                    if(totalBytes!=null && totalBytes>0L){
                        send(DownloadStatus.Progress(ProgressUpdate(bytesSentTotal,totalBytes)))
                    }
                }
            }
            val bytes= httpResponse.readRawBytes()
            val fileInfo= FileInfo(
                name = "$quizId-${rawQuizName.formatQuizName()}",
                mimeType = "application/zip",
                bytes = bytes
            )
            val saved= async {  fileManager.saveTempZipFile(fileInfo)}.await()
            if(saved){
                val status= async { fileManager.unzipAndSaveFiles(fileInfo) }.await()
                if(!status){
                    send(DownloadStatus.ResultState(Result.Error(NetworkError.UNKNOWN)))
                } else Log.d("Yeet","zip file saved and unzipped")
            }
            httpResponse
        }
        send(DownloadStatus.ResultState(downloadResponse))
        }


    override fun pushQuizWithQuestions(quizWithQuestions: QuizWithQuestions): Flow<UploadStatus<UploadResponseDto, NetworkError>> = channelFlow {
        val zipped= async {  fileManager.zipFolder(
            rawQuizName = quizWithQuestions.quiz.title,
            quizId = quizWithQuestions.quiz.quizId
            )}.await()
        if(zipped) {
            val zipFileInfo = fileManager.getFileInfo(quizWithQuestions.quiz.quizId, quizWithQuestions.quiz.title)
            val uploadResponse = safeCall<UploadResponseDto> {
                httpClient.submitFormWithBinaryData(
                    url = constructQuizUrl("/quiz-upload"),
                    formData = formData {
                        append("file", zipFileInfo.bytes, Headers.build {
                            append(HttpHeaders.ContentType, zipFileInfo.mimeType)
                            append(HttpHeaders.ContentDisposition, "filename=${zipFileInfo.name}.zip")
                            append(HttpHeaders.ContentLength, zipFileInfo.bytes.size.toString())
                        })
                    }
                ) {
                    method= HttpMethod.Post
                    onUpload { bytesSentTotal, totalBytes ->
                        if (totalBytes != null && totalBytes > 0L) {
                            send(UploadStatus.Progress(ProgressUpdate(bytesSentTotal, totalBytes)))
                        }
                    }
                }
            }
            send(UploadStatus.ResultState(uploadResponse))
        }
    }


    override suspend fun pushAction(controlMode: ControlMode): EmptyResult<NetworkError> {
        return safeCall{
            httpClient.post(
                urlString = constructQuizUrl("/update-action")
            ){
                contentType(ContentType.Application.Json)
                setBody(controlMode.toControlModeDto())
            }
        }
    }

    override suspend fun pushDisplay(display: Display): EmptyResult<NetworkError> {
        return safeCall {
            httpClient.post(
                urlString = constructQuizUrl("/update-display")
            ){
                contentType(ContentType.Application.Json)
                setBody(display.toDisplayDto())
            }
        }
    }

}
package com.example.steamapp.api.data.networking.repository

import android.os.Build
import android.view.Display
import androidx.annotation.RequiresApi
import com.example.steamapp.api.data.mappers.toQuizEntity
import com.example.steamapp.api.data.mappers.toQuizWithQuestions
import com.example.steamapp.api.data.networking.dto.QuizListDto
import com.example.steamapp.api.data.networking.dto.QuizWithQuestionsDto
import com.example.steamapp.api.data.networking.models.ProgressUpdate
import com.example.steamapp.api.domain.models.ControlMode
import com.example.steamapp.api.data.networking.dto.UploadResponseDto
import com.example.steamapp.api.domain.repository.APIRepository
import com.example.steamapp.core.data.internal_storage.FileManager
import com.example.steamapp.core.data.networking.constructQuizUrl
import com.example.steamapp.core.data.networking.safeCall
import com.example.steamapp.core.util.networking.EmptyResult
import com.example.steamapp.core.util.networking.NetworkError
import com.example.steamapp.core.util.networking.Result
import com.example.steamapp.core.util.networking.UploadStatus
import com.example.steamapp.core.util.networking.map
import com.example.steamapp.quiz_feature.data.local.entities.relations.QuizWithQuestions
import com.example.steamapp.quiz_feature.domain.mappers.toQuiz
import com.example.steamapp.quiz_feature.domain.models.Quiz
import io.ktor.client.HttpClient
import io.ktor.client.plugins.onUpload
import io.ktor.client.request.delete
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
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
        return safeCall<QuizListDto> {
            httpClient.get(
                urlString = constructQuizUrl("/quizzes")
            )
        }.map { quizList->
            quizList.quizzes.map {
                it.toQuizEntity().toQuiz()
            }
        }
    }

    override suspend fun deleteQuizByQuizId(quizId: Long): EmptyResult<NetworkError> {
        return safeCall {
            httpClient.delete("/quiz-delete/${quizId}")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getQuizWithQuestions(quizId: Long): Result<QuizWithQuestions, NetworkError> {
        return safeCall<QuizWithQuestionsDto> {
            httpClient.get(
                urlString = constructQuizUrl("/quiz/$quizId")
            ){
                // download logic
            }
        }.map {
            it.toQuizWithQuestions()
        }
    }

    override fun pushQuizWithQuestions(quizWithQuestions: QuizWithQuestions): Flow<UploadStatus<UploadResponseDto, NetworkError>> = channelFlow {
        fileManager.zipFolder(
            rawQuizName = quizWithQuestions.quiz.title,
            quizId = quizWithQuestions.quiz.quizId
            )
        val zipFileInfo= fileManager.getFileInfo(quizWithQuestions.quiz.quizId, quizWithQuestions.quiz.title)
        val uploadResponse= safeCall<UploadResponseDto> {
            httpClient.submitFormWithBinaryData(
                url = constructQuizUrl("/quiz-upload"),
                formData = formData {
                    append(zipFileInfo.name, zipFileInfo.bytes, Headers.build {
                        append(HttpHeaders.ContentType, zipFileInfo.mimeType)
                        append(HttpHeaders.ContentDisposition, "filename=${zipFileInfo.name}")
                        append(HttpHeaders.ContentLength, zipFileInfo.bytes.size.toString())
                    })
                }
            ){
                onUpload{ bytesSentTotal, totalBytes->
                    if(totalBytes!=null && totalBytes>0L){
                        send(UploadStatus.Progress(ProgressUpdate(bytesSentTotal, totalBytes)))
                    }
                }
            }
        }
        send(UploadStatus.ResultState(uploadResponse))
    }


    override suspend fun pushAction(controlMode: ControlMode): EmptyResult<NetworkError> {
        return safeCall{
            httpClient.post(
                urlString = constructQuizUrl("/action")
            ){
                contentType(ContentType.Application.Json)
                setBody(controlMode)
            }
        }
    }

    override suspend fun pushDisplay(display: Display): EmptyResult<NetworkError> {
        return safeCall {
            httpClient.post(
                urlString = constructQuizUrl("/display")
            ){
                contentType(ContentType.Application.Json)
                setBody(display)
            }
        }
    }

}
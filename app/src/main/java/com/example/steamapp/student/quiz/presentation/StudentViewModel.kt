package com.example.steamapp.student.quiz.presentation

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.example.steamapp.api.domain.repository.APIRepository
import com.example.steamapp.api.presentation.APIEvents
import com.example.steamapp.core.data.internal_storage.FileManager
import com.example.steamapp.core.util.networking.onError
import com.example.steamapp.core.util.networking.onSuccess
import com.example.steamapp.material_feature.domain.models.StudyMaterial
import com.example.steamapp.student.StudentDetail
import com.example.steamapp.student.quiz.domain.models.DummyQuizWithQuestions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.qualifier._q

class StudentViewModel(
    private val apiRepository: APIRepository,
    private val fileManager: FileManager
): ViewModel() {

    private val _studentDetails= MutableStateFlow<StudentDetail?>(null)
    val studentDetails= _studentDetails.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        null
    )

    private val _events= Channel<APIEvents>()
    val events= _events.receiveAsFlow()

    private val _quizState= MutableStateFlow(StudentQuizState())
    @RequiresApi(Build.VERSION_CODES.O)
    val quizState = _quizState
        .onStart {
            loadAllQuizzesAndMaterials()
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), StudentQuizState())


    init {
        fileManager.pushDummyMaterials()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun onAction(actions: StudentQuizActions){
        when(actions){
            is StudentQuizActions.onLoadQuizForTest -> {
                loadSelectedQuiz(actions.id)
            }
            is StudentQuizActions.onClearSelectedQuiz -> { clearSelectedQuiz()}
            is StudentQuizActions.onLoadPdfMaterial -> {loadSelectedMaterial(actions.material)}
            is StudentQuizActions.onClearSelectedMaterial-> {clearSelectedMaterial()}
            is StudentQuizActions.onLoadStudentReport -> {
                loadReport(actions.name)
            }
            is StudentQuizActions.onClearStudentReport -> {
                clearReport()
            }
        }
    }

    private fun loadReport(name: String){
        viewModelScope.launch (Dispatchers.IO) {
            apiRepository.getStudentReport(name)
                .onSuccess { detail->
                    _studentDetails.update {
                        detail
                    }
                }
                .onError {error->
                    _events.send(APIEvents.Error(error))
                }
        }
    }

    private fun clearReport(){
        viewModelScope.launch (Dispatchers.IO) {
            _studentDetails.update { null }
        }
    }

    private fun loadSelectedMaterial(material: StudyMaterial){
        _quizState.update {
            it.copy(isLoading = true)
        }
        viewModelScope.launch {
            _quizState.update {
                it.copy(
                    isLoading = false,
                    selectedMaterial = material
                )
            }
        }
    }

    private fun clearSelectedMaterial(){
        _quizState.update {
            it.copy(selectedMaterial = null)
        }
    }

    private fun clearSelectedQuiz(){
        _quizState.update {
            it.copy(isLoading = true)
        }
        viewModelScope.launch(Dispatchers.IO) {
            _quizState.update {
                it.copy(isLoading = false, selectedQuiz = null)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadAllQuizzesAndMaterials(){
        _quizState.update {
            it.copy(isLoading = true)
        }
        viewModelScope.launch(Dispatchers.IO) {
            _quizState.update {
                it.copy(
                    isLoading = false,
                    quizzes = DummyQuizWithQuestions.quizList,
                    materials = DummyQuizWithQuestions.materialList
                )
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadSelectedQuiz(quizId: Long){
        _quizState.update {
            it.copy(isLoading = true)
        }
        viewModelScope.launch(Dispatchers.IO) {
            val selectedQuiz= DummyQuizWithQuestions.quizList.find { quiz->
                quiz.quiz.quizId == quizId
            }
            _quizState.update {
                it.copy(
                    isLoading = false,
                    selectedQuiz = selectedQuiz
                )
            }
        }
    }


}
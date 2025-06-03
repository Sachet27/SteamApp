package com.example.steamapp.student.quiz.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.steamapp.core.data.internal_storage.FileManager
import com.example.steamapp.material_feature.domain.models.StudyMaterial
import com.example.steamapp.student.quiz.domain.models.DummyQuizWithQuestions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.qualifier._q

class StudentViewModel(
    fileManager: FileManager
): ViewModel() {

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
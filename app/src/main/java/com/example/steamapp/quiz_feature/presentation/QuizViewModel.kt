package com.example.steamapp.quiz_feature.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.steamapp.quiz_feature.domain.repository.QuizRepository
import com.example.steamapp.quiz_feature.presentation.add_and_edit.QuizFormState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class QuizViewModel(
    private val repository: QuizRepository
): ViewModel() {
    private val _quizState= MutableStateFlow(QuizState())
    val quizState: StateFlow<QuizState> = _quizState
        .onStart {
            getAllMyQuizzes()
        }
        .stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        QuizState()
    )

    private fun getAllMyQuizzes(){
        _quizState.update {
            it.copy(isLoading = true)
        }
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllQuizzes().collectLatest {myQuizzes->
                _quizState.update {
                    it.copy(
                        localQuizzes = myQuizzes,
                        isLoading = false
                    )
                }
            }
        }
    }
}
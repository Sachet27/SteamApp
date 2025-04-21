package com.example.steamapp.quiz_feature.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.steamapp.quiz_feature.data.local.entities.QuizEntity
import com.example.steamapp.quiz_feature.data.local.entities.relations.QuizWithQuestions
import com.example.steamapp.quiz_feature.domain.models.Question
import com.example.steamapp.quiz_feature.domain.models.Quiz
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
import java.time.Instant

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

    private val _quizFormState= MutableStateFlow(QuizFormState())
    val quizFormState: StateFlow<QuizFormState> = _quizFormState.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        QuizFormState()
    )

    @RequiresApi(Build.VERSION_CODES.O)
    fun onAction(actions: QuizActions){
        when(actions){
            is QuizActions.onAddQuiz -> { insertQuiz(actions.quiz) }
            is QuizActions.onUpdateQuiz -> { updateQuiz(actions.quiz) }
            is QuizActions.onChangeAnswerIndex -> {changeCorrectOptionIndex(actions.index)}
            is QuizActions.onChangeOptionA -> {changeOptionA(actions.newOptionA)}
            is QuizActions.onChangeOptionB -> {changeOptionB(actions.newOptionB)}
            is QuizActions.onChangeOptionC -> {changeOptionC(actions.newOptionC)}
            is QuizActions.onChangeOptionD -> {changeOptionD(actions.newOptionD)}
            is QuizActions.onChangeTitle -> {changeQuestionTitle(actions.newTitle)}
            QuizActions.onClearFormData -> {clearFormData()}
            is QuizActions.onLoadQuizData -> {
                val quizId= actions.quizId
                quizId?.let {
                    getQuizFormInfo(it)
                }
            }
            is QuizActions.onChangeQuizTitle -> {changeQuizTitle(actions.newQuizTitle)}
            is QuizActions.onChangeQuizDescription -> {changeQuizDescription(actions.newQuizDescription)}
            is QuizActions.onInsertQuestion -> { insertQuestion(actions.question) }
            is QuizActions.onChangeQuestion -> {loadNextQuestion(actions.question)}
            is QuizActions.onUpdateQuestion -> {updateQuestion(actions.question)}
            is QuizActions.onDeleteQuiz -> {deleteQuizWithQuestions(actions.quizId)}
            QuizActions.onClearData -> {
                clearData()
            }
        }
    }

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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getQuizFormInfo(quizId: Long){
        _quizFormState.update {
            it.copy(isLoading = true)
        }
        viewModelScope.launch {
            val quizWithQuestions=repository.getQuizWithQuestionsById(quizId)?: QuizWithQuestions(
                quiz = QuizEntity(0L,"", null, Instant.now(), 0),
                questions = emptyList()
            )
                if(quizWithQuestions.questions.isEmpty()){
                    _quizFormState.update {
                        it.copy(
                            quizId = quizId,
                            isLoading = false,
                            quizWithQuestions = quizWithQuestions,
                        )
                    }
                }else{
                    _quizFormState.update {
                        it.copy(
                            quizId = quizId,
                            isLoading = false,
                            quizWithQuestions = quizWithQuestions,
                            title = quizWithQuestions.questions[0].title,
                            optionA = quizWithQuestions.questions[0].options[0],
                            optionB = quizWithQuestions.questions[0].options[1],
                            optionC = quizWithQuestions.questions[0].options[2],
                            optionD = quizWithQuestions.questions[0].options[3],
                            correctOptionIndex = quizWithQuestions.questions[0].correctOptionIndex,
                            imageRelativePath = quizWithQuestions.questions[0].imageRelativePath,
                            audioRelativePath = quizWithQuestions.questions[0].audioRelativePath,
                            questionCount = quizWithQuestions.quiz.questionCount,
                            quizTitle = quizWithQuestions.quiz.title,
                            quizDescription = quizWithQuestions.quiz.description
                        )
                    }
                }
            }
        }

    private fun insertQuiz(quiz: Quiz){
        _quizFormState.update {
            it.copy(isLoading = true)
        }
        viewModelScope.launch(Dispatchers.IO) {
            val quizId= repository.insertQuiz(quiz)
            _quizFormState.update {
                it.copy(
                    isLoading = false,
                    quizId = quizId
                )
            }
        }
    }

    private fun updateQuiz(quiz: Quiz){
        _quizFormState.update {
            it.copy(isLoading = true)
        }
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateQuiz(quiz)
            _quizFormState.update {
                it.copy(
                    isLoading = false
                )
            }
        }
    }

    private fun insertQuestion(question: Question){
        _quizFormState.update{
            it.copy(isLoading = true)
        }
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertQuestion(question)
            _quizFormState.update {
                it.copy(isLoading = false)
            }
        }
    }

    private fun updateQuestion(question: Question){
        _quizFormState.update{
            it.copy(isLoading = true)
        }
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateQuestion(question)
            _quizFormState.update {
                it.copy(isLoading = false)
            }
        }
    }

    private fun loadNextQuestion(question: Question){
         _quizFormState.update {
             it.copy(
                 title = question.title,
                 optionA = question.options[0],
                 optionB = question.options[1],
                 optionC = question.options[2],
                 optionD = question.options[3],
                 correctOptionIndex = question.correctOptionIndex,
                 imageRelativePath = question.imageRelativePath,
                 audioRelativePath = question.audioRelativePath
             )
         }
    }

    private fun deleteQuizWithQuestions(quizId: Long){
        _quizState.update {
            it.copy(isLoading = true)
        }
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteQuizWithQuestionsByQuizId(quizId)
            _quizState.update {
                it.copy(
                    isLoading = false
                )
            }
        }
    }

    private fun changeQuizTitle(newTitle: String){
        _quizFormState.update {
            it.copy(
                quizTitle = newTitle
            )
        }
    }

    private fun changeQuizDescription(newDescription: String){
        _quizFormState.update {
            it.copy(
                quizDescription = newDescription
            )
        }
    }

    private fun changeQuestionTitle(newTitle: String){
        _quizFormState.update {
            it.copy(
                title = newTitle
            )
        }
    }

    private fun changeOptionA(newOptionA: String){
        _quizFormState.update {
            it.copy(
                optionA = newOptionA
            )
        }
    }

    private fun changeOptionB(newOptionB: String){
        _quizFormState.update {
            it.copy(
                optionB = newOptionB
            )
        }
    }

    private fun changeOptionC(newOptionC: String){
        _quizFormState.update {
            it.copy(
                optionC = newOptionC
            )
        }
    }

    private fun changeOptionD(newOptionD: String){
        _quizFormState.update {
            it.copy(
                optionD = newOptionD
            )
        }
    }

    private fun changeCorrectOptionIndex(newIndex: Int){
        _quizFormState.update {
            it.copy(
                correctOptionIndex = newIndex
            )
        }
    }

    private fun clearFormData() {
        _quizFormState.update {
            it.copy(
                title = "",
                optionA = "",
                optionB = "",
                optionC = "",
                optionD = "",
                correctOptionIndex = 0,
                imageRelativePath =null,
                audioRelativePath= null,
            )
        }
        }

    private fun clearData() {
        _quizFormState.update {
            it.copy(
                quizWithQuestions = null,
                quizId = 0L,
                quizTitle = "",
                quizDescription = null,
                title = "",
                optionA = "",
                optionB = "",
                optionC = "",
                optionD = "",
                correctOptionIndex = 0,
                imageRelativePath =null,
                audioRelativePath= null,
                questionCount = 0
            )
        }
    }
    }
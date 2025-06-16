package com.example.steamapp.material_feature.presentation

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.core.net.toUri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.steamapp.MainActivity
import com.example.steamapp.api.domain.repository.APIRepository
import com.example.steamapp.api.presentation.APIEvents
import com.example.steamapp.core.data.internal_storage.FileManager
import com.example.steamapp.core.presentation.student.StudentListState
import com.example.steamapp.core.util.formatQuizName
import com.example.steamapp.core.util.networking.onError
import com.example.steamapp.core.util.networking.onSuccess
import com.example.steamapp.core.util.transformFilePath
import com.example.steamapp.core.util.transformMaterialFilePath
import com.example.steamapp.material_feature.data.pdf.PdfBitmapConverter
import com.example.steamapp.material_feature.domain.models.StudyMaterial
import com.example.steamapp.material_feature.domain.repository.MaterialRepository
import com.example.steamapp.quiz_feature.data.local.entities.relations.QuizWithQuestions
import com.google.mlkit.vision.documentscanner.GmsDocumentScanner
import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.internal.ChannelFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class MaterialViewModel(
    private val repository: MaterialRepository,
    private val apiRepository: APIRepository,
    private val fileManager: FileManager,
    private val pdfBitmapConverter: PdfBitmapConverter,
    private val pdfScanner: GmsDocumentScanner,
): ViewModel(){

    private val _studentListState= MutableStateFlow(StudentListState())
    val studentListState= _studentListState.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        StudentListState()
    )

    private val _selectedMaterial= MutableStateFlow<StudyMaterial?>(null)
    val selectedMaterial= _selectedMaterial.asStateFlow()

    private val _infoState= MutableStateFlow(MaterialInfoState())
    val infoState= _infoState.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), MaterialInfoState())

    private val _materialState= MutableStateFlow(MaterialState())
    val materialState= _materialState
        .onStart {
            getAllMyMaterials()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            MaterialState()
        )

    private val _events= Channel<APIEvents>()
    val events= _events.receiveAsFlow()

    init {
        getAllPiMaterials()
    }

    fun onAction(actions: MaterialActions){
        when(actions){
            is MaterialActions.onChangeDescription -> {
                changeDescription(actions.newDescription)
            }
            is MaterialActions.onChangeTitle -> {changeTitle(actions.newTitle)}
            MaterialActions.onClearMaterialInfo -> {clearMaterialInfoState()}
            is MaterialActions.onDeleteMaterial -> {deleteMaterial(actions.material)}
            is MaterialActions.onInsertMaterial -> {insertMaterial(actions.uri, actions.context)}
            MaterialActions.onRefreshMaterials -> {
                getAllPiMaterials()
            }
            is MaterialActions.onSelectMaterial -> {selectMaterial(id = actions.id, callBack = actions.callBack)}
            is MaterialActions.onLoadDisplayMaterial -> {loadDisplayMaterial(actions.material)}
            is MaterialActions.onRenderPages -> renderPages(actions.context, actions.material, actions.callBack)
            is MaterialActions.onClearSelectedStudentReport -> { clearSelectedReport() }
            is MaterialActions.onLoadStudentReport -> { loadSelectedStudentReport(actions.name)}
            is MaterialActions.onLoadStudentsList -> {  loadStudentList()}
        }
    }

    private fun loadSelectedStudentReport(name: String){
        _studentListState.update {
            it.copy(isLoading = true)
        }
        viewModelScope.launch (Dispatchers.IO) {
            apiRepository.getStudentReport(name = name)
                .onSuccess { report->
                    _studentListState.update {
                        it.copy(isLoading = false, selectedStudentReport = report)
                    }
                }
                .onError { error->
                    _events.send(APIEvents.Error(error))
                    _studentListState.update {
                        it.copy(isLoading = false)
                    }
                }
        }
    }

    private fun clearSelectedReport(){
        _studentListState.update {
            it.copy(selectedStudentReport = null )
        }
    }

    private fun loadStudentList(){
        _studentListState.update { it.copy(isLoading = true) }
        viewModelScope.launch (Dispatchers.IO) {
            apiRepository.getStudentList()
                .onSuccess {students->
                    _studentListState.update {
                        it.copy(
                            isLoading = false,
                            studentList = students
                        )
                    }
                }
                .onError { error->
                    _events.send(APIEvents.Error(error))
                    _studentListState.update {
                        it.copy(isLoading = false)
                    }
                }
        }
    }


    private fun renderPages(context: Context, studyMaterial: StudyMaterial, callBack: ((List<Bitmap>) -> Unit)?){
        viewModelScope.launch {
            val pages= pdfBitmapConverter.pdfToBitmaps(studyMaterial.pdfUri.toUri())
            callBack?.invoke(pages)
        }
    }

    private fun loadDisplayMaterial(material: StudyMaterial){
        _materialState.update {
            it.copy(isLoading = true)
        }
        viewModelScope.launch (Dispatchers.IO){
            val foundMaterial= async {
                fileManager.getMaterialFromJson(material.id, material.name)
            }.await()
            _selectedMaterial.update {
                foundMaterial
            }
            _materialState.update {
                it.copy(isLoading = false)
            }
        }
    }

    private fun getAllPiMaterials(){
            _materialState.update {
                it.copy(isLoading = true)
            }
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    apiRepository.getMaterials()
                        .onSuccess { materials ->
                            _materialState.update {
                                it.copy(
                                    piMaterials = materials,
                                    isLoading = false
                                )
                            }
                        }
                        .onError {
                            _events.send(APIEvents.Error(it))
                        }
                }catch(e:Exception){
                    Log.d("Yeet", "material viewmodel: ${e.stackTraceToString()}")
                }
            }
    }

    private fun selectMaterial(id: Long, callBack: ((StudyMaterial?)->Unit)?){
        _materialState.update {
            it.copy(
                isLoading = true
            )
        }
        viewModelScope.launch(Dispatchers.IO) {
            val quiz= async { repository.getMaterialById(id) }.await()
            _materialState.update {
                it.copy(
                    isLoading = false,
                )
            }
            callBack?.invoke(quiz)
        }
    }


    fun getStartScanIntent(context: Context, onSuccess: (IntentSender)->Unit, onError: (String)->Unit){
        pdfScanner.getStartScanIntent(context as Activity)
            .addOnSuccessListener {
                onSuccess(it)
            }
            .addOnFailureListener{
                onError(it.message ?: "Unknown error")
            }
    }

    fun handleScanResult(data: Intent?, context: Context){
        viewModelScope.launch {
            val result= GmsDocumentScanningResult.fromActivityResultIntent(data)
            result?.pdf?.let { pdf->
                insertMaterial(pdf.uri, context)
            }?: Log.d("Yeet", "Viewmodel: result is null")
        }
    }



    private fun getAllMyMaterials(){
        _materialState.update {
            it.copy(isLoading = true)
        }
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllMaterials().collectLatest { materials->
                _materialState.update {
                    it.copy(
                        isLoading = false,
                        myMaterials = materials
                    )
                }
            }
        }
    }

    private fun getMaterialById(id: Long){
        _materialState.update {
            it.copy(isLoading = true)
        }
        viewModelScope.launch(Dispatchers.IO) {
            val material= repository.getMaterialById(id)
            _materialState.update {
                it.copy(
                    isLoading = false,
                    selectedMaterial = material
                )
            }
        }
    }

    private fun insertMaterial(uri: Uri, context: Context){
        _materialState.update {
            it.copy(isLoading = true)
        }
        viewModelScope.launch(Dispatchers.IO) {
            val pages= async { fileManager.savePdf(
                name = _infoState.value.title,
                uri = uri
            )}.await()
            if(pages>0){
                val folderName= infoState.value.title.formatQuizName()
                val material= StudyMaterial(
                    id = 0L,
                    name = infoState.value.title,
                    description = infoState.value.description,
                    pdfUri = "materials/${folderName}/${folderName}.pdf",
                    pages = pages
                )
                val id= async { repository.insertMaterial(material)}.await()
                //save json
                val updatedMaterial = material.copy(
                    id= id,
                    pdfUri = transformMaterialFilePath(material.pdfUri, id)
                )
                val saved= async {  fileManager.saveMaterialJson(updatedMaterial) }.await()
                if(saved){
                    fileManager.prefixFolderWithMaterialId(
                        name = updatedMaterial.name,
                        id = updatedMaterial.id
                    )
                    withContext(Dispatchers.Main){
                        Toast.makeText(context, "Successfully added!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            _materialState.update {
                it.copy(
                    isLoading = false
                )
            }
            clearMaterialInfoState()
        }
    }

    private fun updateMaterial(material: StudyMaterial){
        _materialState.update {
            it.copy(isLoading = true)
        }
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateMaterial(material)
            _materialState.update {
                it.copy(
                    isLoading = false
                )
            }
        }
    }

    private fun deleteMaterial(material: StudyMaterial){
        _materialState.update {
            it.copy(isLoading = true)
        }
        viewModelScope.launch(Dispatchers.IO) {
            fileManager.deleteMaterialFolderContents(material.name, material.id)
            repository.deleteMaterial(material)
            _materialState.update {
                it.copy(
                    isLoading = false
                )
            }
        }
    }

    private fun changeTitle(newTitle: String){
        _infoState.update {
            it.copy(title = newTitle)
        }
    }

    private fun changeDescription(newDescription: String){
        _infoState.update {
            it.copy(description = newDescription)
        }
    }

    private fun clearMaterialInfoState(){
        _infoState.update {
            it.copy(
                title = "Untitled",
                description = null
            )
        }
    }
}


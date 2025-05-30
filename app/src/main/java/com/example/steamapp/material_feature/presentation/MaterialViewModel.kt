package com.example.steamapp.material_feature.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.steamapp.material_feature.domain.models.StudyMaterial
import com.example.steamapp.material_feature.domain.repository.MaterialRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MaterialViewModel(
    private val repository: MaterialRepository
): ViewModel(){

    private val _materialState= MutableStateFlow(MaterialState())
    val materialState= _materialState
        .onStart {
            getAllMaterials()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            MaterialState()
        )



    private fun getAllMaterials(){
        _materialState.update {
            it.copy(isLoading = true)
        }
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllMaterials().collectLatest { materials->
                _materialState.update {
                    it.copy(
                        isLoading = false,
                        materials = materials
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

    private fun insertMaterial(material: StudyMaterial){
        _materialState.update {
            it.copy(isLoading = true)
        }
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertMaterial(material)
            _materialState.update {
                it.copy(
                    isLoading = false
                )
            }
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
            repository.deleteMaterial(material)
            _materialState.update {
                it.copy(
                    isLoading = false
                )
            }
        }
    }


}


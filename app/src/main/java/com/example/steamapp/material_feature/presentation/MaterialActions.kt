package com.example.steamapp.material_feature.presentation

import android.content.Context
import android.net.Uri
import com.example.steamapp.material_feature.domain.models.StudyMaterial

sealed class MaterialActions{
    data class onLoadDisplayMaterial(val material: StudyMaterial): MaterialActions()
    data class onSelectMaterial(val id: Long, val callBack: ((StudyMaterial?)->Unit)? = null): MaterialActions()
    data class onChangeTitle(val newTitle: String): MaterialActions()
    data class onChangeDescription(val newDescription: String): MaterialActions()
    data object onClearMaterialInfo : MaterialActions()

    data class onInsertMaterial(val uri: Uri, val context: Context): MaterialActions()
    data class onDeleteMaterial(val material: StudyMaterial): MaterialActions()
    data object onRefreshMaterials: MaterialActions()
}

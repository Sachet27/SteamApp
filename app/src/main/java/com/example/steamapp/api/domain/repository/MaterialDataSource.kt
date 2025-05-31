package com.example.steamapp.api.domain.repository


import com.example.steamapp.core.util.networking.DownloadStatus
import com.example.steamapp.core.util.networking.EmptyResult
import com.example.steamapp.core.util.networking.NetworkError
import com.example.steamapp.core.util.networking.Result
import com.example.steamapp.core.util.networking.UploadStatus
import com.example.steamapp.material_feature.domain.models.StudyMaterial
import kotlinx.coroutines.flow.Flow

interface MaterialDataSource {
        suspend fun getMaterials(): Result<List<StudyMaterial>, NetworkError>
        fun getMaterial(id: Long, name: String): Flow<DownloadStatus<Unit, NetworkError>>
        fun pushMaterial(material: StudyMaterial): Flow<UploadStatus<Unit, NetworkError>>
        suspend fun deleteMaterialById(id: Long, name: String): EmptyResult<NetworkError>
}
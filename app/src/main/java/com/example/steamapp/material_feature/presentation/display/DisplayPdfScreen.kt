package com.example.steamapp.material_feature.presentation.display

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.room.util.TableInfo
import com.example.steamapp.api.presentation.APIActions
import com.example.steamapp.material_feature.domain.models.StudyMaterial


@Composable
fun DisplayPdfScreen(
    modifier: Modifier= Modifier,
    syncWithPi:Boolean,
    material: StudyMaterial,
    onAPIActions: (APIActions)->Unit,
    onBackNav: ()-> Unit
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState()),
    ) {

    }
}
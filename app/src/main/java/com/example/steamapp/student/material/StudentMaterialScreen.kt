package com.example.steamapp.student.material

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.steamapp.material_feature.presentation.MaterialActions
import com.example.steamapp.material_feature.presentation.components.MaterialCard
import com.example.steamapp.material_feature.presentation.home.MaterialScreen
import com.example.steamapp.quiz_feature.domain.mappers.toQuiz
import com.example.steamapp.quiz_feature.presentation.home.components.QuizCard
import com.example.steamapp.student.quiz.domain.models.MaterialType
import com.example.steamapp.student.quiz.presentation.StudentQuizActions
import com.example.steamapp.student.quiz.presentation.StudentQuizState

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun StudentMaterialScreen(
    modifier: Modifier = Modifier,
    state: StudentQuizState,
    onAction: (StudentQuizActions)->Unit,
    onNavToDisplayPdfScreen: ()->Unit,
    onNavToVideoScreen: ()->Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .background(MaterialTheme.colorScheme.background),
    ) {
        Text(
            text = "Materials !",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        Spacer(Modifier.height(2.dp))
        Text(
            text = "Here are some materials, happy reading!",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        Spacer(Modifier.height(8.dp))
        LazyColumn(
            modifier = Modifier.padding(8.dp)
        ) {
            items(state.materials){material->
                MaterialCard(
                    material = material,
                    icon = null,
                    onClick = {
                        onAction(StudentQuizActions.onLoadPdfMaterial(material))
                        if(material.materialType== MaterialType.PDF){
                            onNavToDisplayPdfScreen()
                        } else{
                            onNavToVideoScreen()
                        }
                    },
                    onDelete = {},
                    onIconClick = {}
                )
                Spacer(Modifier.height(12.dp))
            }
        }
    }
}
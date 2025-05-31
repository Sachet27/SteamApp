package com.example.steamapp.material_feature.presentation


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.steamapp.R
import com.example.steamapp.material_feature.domain.models.StudyMaterial
import com.example.steamapp.material_feature.presentation.components.MaterialCard
import com.example.steamapp.ui.theme.SteamAppTheme

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MaterialScreen(
    modifier: Modifier = Modifier,
    state: MaterialState,
    onMaterialAction: (MaterialActions)->Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        val context= LocalContext.current

        if(state.isLoading){
            Column(
                modifier= modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
                Text(
                    text = "Loading data",
                    color = MaterialTheme.colorScheme.secondary,
                    fontSize = 16.sp
                )
            }
        }
        else if(state.materials.isEmpty()){
            Column(
                modifier= Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.emptyquizzes),
                    contentDescription = "No Materials yet :(",
                    modifier = Modifier.size(300.dp),
                    alpha = 0.8f
                )
                Text(
                    text = "No Quizzes Yet.",
                    color = MaterialTheme.colorScheme.secondary,
                    fontSize = 16.sp
                )
            }
        } else{
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(state.materials){ material->
                    MaterialCard(
                        material = material,
                        icon = R.drawable.push_to_raspberry_pi_icon,
                        onClick = {

                        },
                        onDelete = {

                        },
                        onIconClick = {

                        }
                    )
                    Spacer(Modifier.height(4.dp))
                }
            }
        }

    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun MaterialPreview() {
    val dummyList= listOf(
        StudyMaterial(id = 1, name = "Height and Distance", description = "Class 10, chapter 7", pdfUri = "hello/world", pages = 17),
        StudyMaterial(id = 2, name = "Trigonometry", description = "Class 9, chapter 1", pdfUri = "hello/world2", pages = 20),
        StudyMaterial(id = 3, name = "History of Nepal", description = "Class 8, chapter 3", pdfUri = "hello/world", pages = 9),
    )
    SteamAppTheme {
        MaterialScreen(
            state = MaterialState(
                isLoading = false,
                materials = dummyList
            ),
            onMaterialAction = {}
        )
    }
}
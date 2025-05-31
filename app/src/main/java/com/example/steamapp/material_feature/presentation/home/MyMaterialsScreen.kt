package com.example.steamapp.material_feature.presentation.home

import android.os.Build
import android.widget.Toast
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.steamapp.R
import com.example.steamapp.api.presentation.APIActions
import com.example.steamapp.api.presentation.UploadState
import com.example.steamapp.api.presentation.components.UploadDialogBox
import com.example.steamapp.material_feature.domain.models.StudyMaterial
import com.example.steamapp.material_feature.presentation.MaterialActions
import com.example.steamapp.material_feature.presentation.MaterialState
import com.example.steamapp.material_feature.presentation.components.MaterialCard
import com.example.steamapp.quiz_feature.presentation.QuizActions

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MyMaterialsScreen(
    uploadState: UploadState,
    modifier: Modifier = Modifier,
    state: MaterialState,
    onMaterialAction: (MaterialActions)->Unit,
    onAPIAction: (APIActions)->Unit
) {
    val context= LocalContext.current
    var pushedMaterial by remember { mutableStateOf<StudyMaterial?>(null) }
    var showUploadDialog by remember { mutableStateOf(false) }


    if(showUploadDialog){
        UploadDialogBox(
            uploadState = uploadState,
            onCancel = {
                onAPIAction(APIActions.onCancelMaterialUpload)
                val message= it?: "Upload was cancelled."
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                showUploadDialog= false
            },
            onDone = {
                showUploadDialog = false
                pushedMaterial?.let { material ->
                    onMaterialAction(MaterialActions.onDeleteMaterial(material))
                    pushedMaterial = null
                }
                onMaterialAction(MaterialActions.onRefreshMaterials)
            }
        )
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

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
        else if(state.myMaterials.isEmpty()){
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
                    text = "No Materials Yet.",
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
                items(state.myMaterials, key= {it.id}){ material->
                    MaterialCard(
                        material = material,
                        icon = R.drawable.push_to_raspberry_pi_icon,
                        onClick = {
                            //preview haala
                        },
                        onDelete = {
                            onMaterialAction(MaterialActions.onDeleteMaterial(material))
                        },
                        onIconClick = {
                                 onMaterialAction(MaterialActions.onSelectMaterial(material.id){ material->
                                    material?.let{
                                        onAPIAction(APIActions.onPushMaterialToPi(it))
                                        showUploadDialog= true
                                        pushedMaterial= material
                                    }
                                }
                                )
                            }
                    )
                    Spacer(Modifier.height(4.dp))
                }
            }
        }
    }

}
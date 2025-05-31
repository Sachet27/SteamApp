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
import com.example.steamapp.api.presentation.components.DownloadDialogBox
import com.example.steamapp.api.presentation.components.DownloadState
import com.example.steamapp.material_feature.domain.models.StudyMaterial
import com.example.steamapp.material_feature.presentation.MaterialActions
import com.example.steamapp.material_feature.presentation.MaterialState
import com.example.steamapp.material_feature.presentation.components.MaterialCard
import com.example.steamapp.quiz_feature.presentation.QuizActions

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RaspberryPiMaterialsScreen(
    modifier: Modifier = Modifier,
    state: MaterialState,
    downloadState: DownloadState,
    onAPIActions: (APIActions)->Unit,
    onMaterialActions: (MaterialActions)->Unit,
    onNavToDisplayPdfScreen: (Boolean)->Unit
) {
    var selectedMaterial by remember { mutableStateOf<StudyMaterial?>(null) }
    var notSyncWithPi by remember { mutableStateOf(false) }
    var showDownloadDialog by remember { mutableStateOf(false) }
    val context= LocalContext.current

    if(showDownloadDialog){
        DownloadDialogBox(
            downloadState = downloadState,
            onCancel = {
                onAPIActions(APIActions.onCancelMaterialDownload)
                val message= it?: "Download was cancelled."
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                showDownloadDialog= false
            },
            onDone = {
                    selectedMaterial?.let {material->
                        onMaterialActions(MaterialActions.onLoadDisplayMaterial(material))
                        onNavToDisplayPdfScreen(notSyncWithPi)
                        if(!notSyncWithPi){
                            onAPIActions(APIActions.onPresentPdf(material))
                        }
                }
                selectedMaterial = null
                notSyncWithPi= false
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
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
                items(state.myMaterials, key = {it.id}){ material->
                    MaterialCard(
                        material = material,
                        icon = R.drawable.presentation_icon,
                        onClick = { //preview
                            onAPIActions(APIActions.onDownloadMaterialFromPi(material = material))
                            showDownloadDialog= true
                            selectedMaterial= material
                            notSyncWithPi= true
                        },
                        onDelete = {
                            onAPIActions(APIActions.onDeleteMaterialFromPi(material))
                            onMaterialActions(MaterialActions.onRefreshMaterials)
                            onMaterialActions(MaterialActions.onRefreshMaterials)
                        },
                        onIconClick = { //present
                            onAPIActions(APIActions.onDownloadMaterialFromPi(material = material))
                            showDownloadDialog= true
                            selectedMaterial= material
                            notSyncWithPi= false
                        }
                    )
                    Spacer(Modifier.height(4.dp))
                }
            }
        }

    }
}
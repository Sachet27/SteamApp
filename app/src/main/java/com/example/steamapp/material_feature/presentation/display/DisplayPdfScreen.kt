package com.example.steamapp.material_feature.presentation.display

import android.graphics.Bitmap
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.steamapp.R
import com.example.steamapp.api.presentation.APIActions
import com.example.steamapp.material_feature.domain.models.StudyMaterial
import com.example.steamapp.material_feature.presentation.MaterialActions
import com.example.steamapp.ui.theme.SteamAppTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisplayPdfScreen(
    modifier: Modifier= Modifier,
    syncWithPi:Boolean,
    material: StudyMaterial,
    onAPIActions: (APIActions)->Unit,
    onBackNav: ()-> Unit,
    onMaterialActions: (MaterialActions)-> Unit
){
    val context= LocalContext.current
    var pageIndex by remember { mutableStateOf(0) }
    var renderedPages by remember { mutableStateOf<List<Bitmap>>(emptyList()) }

    LaunchedEffect(material) {
        onMaterialActions(MaterialActions.onRenderPages(context, material){ pages->
            renderedPages= pages
        })
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = material.name,
                        fontWeight = FontWeight.W500,
                        style = MaterialTheme.typography.headlineLarge,
                        modifier = Modifier.padding(16.dp),
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onBackNav()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "Back Nav icon",
                            tint= MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                )
            )
        }
    ) {padding->
        if(renderedPages.isEmpty()){
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ){
                CircularProgressIndicator()
            }
        } else{
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                PdfPage(
                    modifier = Modifier.weight(1f),
                    page = renderedPages[pageIndex]
                )
                Row(
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 12.dp)
                        .clip(RoundedCornerShape(40)),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    IconButton(
                        enabled = pageIndex>0,
                        onClick = {
                            pageIndex--
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "previous",
                        )
                    }

                    Text(
                        textAlign = TextAlign.Center,
                        text = "Page: ${pageIndex+1} of ${material.pages}",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    IconButton(
                        enabled = pageIndex < material.pages-1,
                        onClick = {
                            pageIndex++
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = "previous",
                        )
                    }

                }
            }

        }

    }
}


@Composable
fun PdfPage(
    modifier: Modifier = Modifier,
    page: Bitmap
) {
    AsyncImage(
        model = page,
        contentDescription = null,
        modifier= modifier
            .fillMaxWidth()
            .aspectRatio(page.width.toFloat() / page.height.toFloat())
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun DisplayPreview() {
    SteamAppTheme {
        DisplayPdfScreen(
            syncWithPi = false,
            material = StudyMaterial(
                1,
                name = "Astronomy",
                description = "Simple chapter bout stars",
                pdfUri = "sds",
                pages = 17,
            ),
            onAPIActions = {},
            onBackNav = {}
        ) { }
    }
}
package com.example.steamapp.material_feature.data.pdf

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.createBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PdfBitmapConverter(
    private val context: Context
) {
    var renderer: PdfRenderer?= null

    suspend fun pdfToBitmaps(contentUri: Uri): List<Bitmap>{
        return withContext(Dispatchers.IO){
            renderer?.close()
            context.contentResolver.openFileDescriptor(contentUri,"r")?.use{ descriptor->
                with(PdfRenderer(descriptor)){
                    renderer= this
                    return@withContext (0 until pageCount).map{index->
                        openPage(index).use {page->
                            val bitmap= createBitmap(page.width, page.height)
                            val canvas= Canvas(bitmap).apply {
                                drawColor(Color.White.toArgb())
                                drawBitmap(bitmap, 0f, 0f, null)
                            }

                            page.render(
                                bitmap,
                                null,
                                null,
                                PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY
                            )
                            bitmap
                        }
                    }
                }
            }
            return@withContext emptyList()
        }
    }


}
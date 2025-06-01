package com.example.steamapp.material_feature.data.pdf

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.createBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class PdfBitmapConverter(
    private val context: Context
) {
    var renderer: PdfRenderer?= null

    @SuppressLint("SuspiciousIndentation")
    suspend fun pdfToBitmaps(uri: Uri): List<Bitmap>{
        return withContext(Dispatchers.IO){
            renderer?.close()
            val file= File(context.filesDir, uri.toString())
            Log.d("Yeet", file.absolutePath)
            val descriptor= ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
                with(PdfRenderer(descriptor)){
                    renderer= this
                    return@withContext (0 until pageCount).map{index->
                        openPage(index).use {page->
                            val scaleFactor= 3
                            val width= page.width* scaleFactor
                            val height= page.height* scaleFactor
                            val bitmap= createBitmap(width, height)
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
            return@withContext emptyList()
        }
    }


}
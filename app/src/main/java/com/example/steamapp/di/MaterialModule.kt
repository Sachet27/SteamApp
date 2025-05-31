package com.example.steamapp.di

import com.example.steamapp.material_feature.data.pdf.PdfBitmapConverter
import com.example.steamapp.material_feature.data.repository.MaterialRepositoryImpl
import com.example.steamapp.material_feature.domain.repository.MaterialRepository
import com.example.steamapp.quiz_feature.data.local.database.QuizDatabase
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import com.example.steamapp.material_feature.presentation.MaterialViewModel
import com.google.mlkit.vision.documentscanner.GmsDocumentScanner
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.RESULT_FORMAT_JPEG
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.RESULT_FORMAT_PDF
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.SCANNER_MODE_FULL
import com.google.mlkit.vision.documentscanner.GmsDocumentScanning
import org.koin.android.ext.koin.androidContext

val materialModule= module {
    single<PdfBitmapConverter>{
        PdfBitmapConverter(androidContext())
    }

    single<GmsDocumentScannerOptions> {
        GmsDocumentScannerOptions.Builder()
            .setScannerMode(SCANNER_MODE_FULL)
            .setGalleryImportAllowed(true)
            .setPageLimit(20)
            .setResultFormats(RESULT_FORMAT_PDF, RESULT_FORMAT_JPEG)
            .build()
    }

    single<GmsDocumentScanner> {
        GmsDocumentScanning.getClient(get())
    }
    single<MaterialRepository>{
        MaterialRepositoryImpl(
            dao = get<QuizDatabase>().materialDao
        )
    }
    viewModelOf(::MaterialViewModel)
}
package com.example.steamapp.core.data.internal_storage

import android.content.Context
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.util.Log
import android.webkit.MimeTypeMap
import com.example.steamapp.api.domain.models.FileInfo
import com.example.steamapp.core.util.formatQuizName
import com.example.steamapp.material_feature.domain.models.StudyMaterial
import com.example.steamapp.material_feature.presentation.MaterialActions
import com.example.steamapp.quiz_feature.data.local.entities.relations.QuizWithQuestions
import kotlinx.serialization.json.Json
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

class FileManager(
    private val context: Context
){
    fun getFileInfo(quizId: Long, rawQuizName: String): FileInfo{
        val fileName= "$quizId-${rawQuizName.formatQuizName()}"
        val fullFileName= "${fileName}.zip"

        val folder= File(context.filesDir, "zips")
        if(!folder.exists() || !folder.isDirectory) throw IOException("Folder doesn't exist")

        val zipFile= File(folder, fullFileName)
        if(!zipFile.exists() || !zipFile.isFile) throw IOException("The zip file couldn't be created.")

        val mimeType= MimeTypeMap.getSingleton().getMimeTypeFromExtension(zipFile.extension)?:""
        val bytes= BufferedInputStream(FileInputStream(zipFile)).use {inputStream->
            inputStream.readBytes()
        }

        return FileInfo(
            name = fileName,
            mimeType = mimeType,
            bytes = bytes
        )
    }

    fun saveImageOrAudio(uri: Uri, rawQuizName: String, questionId: Long, quizId: Long):String{
        val quizName= if(quizId==0L) rawQuizName.formatQuizName() else updateFolderName(quizId = quizId, rawQuizName = rawQuizName)
        val bytes= context.contentResolver.openInputStream(uri)?.use{ inputStream->
            inputStream.readBytes()
        }?: byteArrayOf()

        val mimeType= context.contentResolver.getType(uri)?:""
        var name= when{
            mimeType.startsWith("image/")-> "${questionId}_image"
            mimeType.startsWith("audio/")-> "${questionId}_audio"
            else-> ""
         }
        if(quizId!=0L) name= "${quizId}_${name}"
        val extension= MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType)
        val fullFileName= "${name}.${extension}"
        val quizFolder= File(context.filesDir, "/quizzes/$quizName")
        if(!quizFolder.exists()){
            quizFolder.mkdirs()
        }
        val file= File(quizFolder , fullFileName)
        if(file.exists()){
            if(!deleteFile(file)){
                throw IOException("Failed to delete existing media files before saving")
            }
        }
        FileOutputStream(file).use { outputStream->
            outputStream.write(bytes)
        }
        return file.absolutePath
    }

    fun saveJson(quizWithQuestions: QuizWithQuestions){
        val quizName= if(quizWithQuestions.quiz.quizId== 0L)
            quizWithQuestions.quiz.title.formatQuizName()
        else updateFolderName(quizId = quizWithQuestions.quiz.quizId, rawQuizName = quizWithQuestions.quiz.title)
        val quizJsonString= Json.encodeToString(QuizWithQuestions.serializer() ,quizWithQuestions)
        Log.d("Yeet" , "Save Json: $quizJsonString")
        val folder= File(context.filesDir, "/quizzes/${quizName}")
        if(!folder.exists()){
            folder.mkdirs()
        }
        val jsonFile= File(folder, "questions.json")
        if(jsonFile.exists()){
            if(!deleteFile(jsonFile)){
                throw IOException("Failed to delete existing file before replacing")
            }
        }
        jsonFile.writeText(quizJsonString)
    }

    fun getQuizWithQuestionsFromJson(quizId: Long, rawQuizName: String): QuizWithQuestions{
        val downloadedFolderName= "$quizId-${rawQuizName.formatQuizName()}"
        val folder= File(context.filesDir, "quizzes/$downloadedFolderName")
        if(!folder.exists() || !folder.isDirectory) throw IOException("The download folder does not exist")

        val jsonFile= File(folder, "questions.json")
        if(!jsonFile.exists()) throw IOException("Unexpected behavior. Json doesn't exist")

        val jsonString= jsonFile.readText()
        val quizWithQuestions= Json.decodeFromString(QuizWithQuestions.serializer(), jsonString)
        return quizWithQuestions
    }

    fun getMaterialFromJson(id: Long, name: String): StudyMaterial{
        val downloadedFolderName= "$id-${name.formatQuizName()}"
        val folder= File(context.filesDir, "materials/$downloadedFolderName")
        if(!folder.exists() || !folder.isDirectory) throw IOException("The download folder does not exist")

        val jsonFile= File(folder, "material.json")
        if(!jsonFile.exists()) throw IOException("Unexpected behavior. Json doesn't exist")

        val jsonString= jsonFile.readText()
        val material= Json.decodeFromString(StudyMaterial.serializer(), jsonString)
        return material
    }


    private fun updateFolderName(quizId: Long, rawQuizName: String): String{
        val parentFolder= File(context.filesDir, "quizzes")
        val newFolderName= "${quizId}-${rawQuizName.formatQuizName()}"
        val newFolder= File(parentFolder, newFolderName)
        val oldFolder= parentFolder.listFiles()?.find {file->
            file.name.startsWith("${quizId}-")
        }
        if(oldFolder==null){
            return rawQuizName.formatQuizName()
        } else{
            if( !oldFolder.exists() || !oldFolder.isDirectory() ){
                throw IOException("The existing file is not a directory")
            }
            if(!oldFolder.renameTo(newFolder)){
                throw IOException("Failed to rename the folder")
            }
            return newFolderName
        }
    }

    fun zipFolder(rawQuizName: String, quizId: Long): Boolean{
        var status: Boolean
        val folderName= "$quizId-${rawQuizName.formatQuizName()}"
        val folderToZip= File("${context.filesDir}/quizzes/$folderName")
        val zipFolder= File(context.filesDir, "zips")
        if(!zipFolder.exists()){
            zipFolder.mkdir()
        }
        val zipFile= File(zipFolder, "$folderName.zip")
        if(zipFile.exists()) deleteFile(zipFile)
        ZipOutputStream(BufferedOutputStream(FileOutputStream(zipFile))).use { outputStream->
           status = zipFiles(folderToZip, "", outputStream)
        }
        return status
    }

    private fun zipFiles(fileToZip: File, parentDirPath: String, zipOutputStream: ZipOutputStream):Boolean {
        if (fileToZip.isHidden) return false

        if (fileToZip.isDirectory) {
            val folderName = if (parentDirPath.isEmpty()) fileToZip.name else "$parentDirPath/${fileToZip.name}"
            val children= fileToZip.listFiles()
            if(children!=null){
                for(childFile in children){
                    zipFiles(childFile, folderName, zipOutputStream)
                }
            }
        } else{
            FileInputStream(fileToZip).use{ inputStream->
                val zipEntry= ZipEntry("$parentDirPath/${fileToZip.name}")
                zipOutputStream.putNextEntry(zipEntry)
                inputStream.copyTo(zipOutputStream)
                zipOutputStream.closeEntry()
            }
        }
        return true
    }

     fun saveTempZipFile(fileInfo: FileInfo):Boolean{
       return try{
            val extension= MimeTypeMap.getSingleton().getExtensionFromMimeType(fileInfo.mimeType)
            val fullFileName= "${fileInfo.name}.$extension"

            val downloadFolder= File(context.filesDir, "quizzes")
            if(!downloadFolder.exists()) {
                downloadFolder.mkdirs()
            }
            val zipFile= File(downloadFolder, fullFileName)
           if(zipFile.exists()){
               true
           }else{
               BufferedOutputStream(FileOutputStream(zipFile)).use{outputStream->
                   outputStream.write(fileInfo.bytes)
               }
               true
           }
        } catch (e:Exception){
            Log.d("Yeet", e.stackTraceToString())
            false
        }
    }

     fun unzipAndSaveFiles(fileInfo: FileInfo): Boolean{
        val extension= MimeTypeMap.getSingleton().getExtensionFromMimeType(fileInfo.mimeType)
        val fullFileName= "${fileInfo.name}.$extension"
        val folder= File(context.filesDir, "quizzes")
        if(!folder.exists()) {
            throw Exception("Folder not detected for zip file")
        }
        //if folder already exists, no need to unzip again
         val resultFolder= File(context.filesDir, fileInfo.name)
         if(resultFolder.exists()) return true

         val zipFile= File(folder, fullFileName)
        if(!zipFile.exists() || !zipFile.isFile) return false

        return try {
            ZipInputStream(FileInputStream(zipFile)).use {inputStream->
                var entry = inputStream.nextEntry
                while(entry!=null){
                    val outputFile= File(folder, entry.name)
                    if(entry.isDirectory){
                        outputFile.mkdirs()
                    } else{
                     outputFile.parentFile?.mkdirs()
                     BufferedOutputStream(FileOutputStream(outputFile)).use {outputStream->
                         inputStream.copyTo(outputStream)
                     }
                    }
                    inputStream.closeEntry()
                    entry= inputStream.nextEntry
                }
            }
            deleteFile(zipFile)
            true
        } catch (e:Exception){
            Log.d("Yeet", "In UnzipFunction: ${e.stackTraceToString()}")
            false
        }
    }

    fun prefixWithQuizId(rawQuizName: String, quizId:Long){
        val quizName= rawQuizName.formatQuizName()
        val folder= File(context.filesDir, "quizzes/$quizName")
        if(!folder.exists() || !folder.isDirectory) return

        val files= folder.listFiles()?: return
        for(file in files){
            if(file.isFile && file.name!= "questions.json"){
                val newFileName= "${quizId}_${file.name}"
                val updatedFile= File(folder, newFileName)

                if(!file.renameTo(updatedFile)) throw IOException("File couldn't be renamed")
            }
        }
        appendQuizFolderWithQuizId(rawQuizName, quizId)
    }

     fun appendQuizFolderWithQuizId(rawQuizName: String, quizId: Long){
        val quizName= rawQuizName.formatQuizName()
        val folder= File(context.filesDir, "quizzes/$quizName")
        if(!folder.exists() || !folder.isDirectory) return
        val parentFolder= File(context.filesDir, "quizzes")
        val newFolderName= "${quizId}-${quizName}"
        val updatedFolder= File(parentFolder, newFolderName)
        if(!folder.renameTo(updatedFolder)) throw IOException("Folder couldn't be renamed")
    }


    fun deleteFolderContents(rawQuizName: String, quizId: Long): Boolean{
        val quizName= "$quizId-${rawQuizName.formatQuizName()}"
        val folder= File(context.filesDir, "quizzes/${quizName}")
        if(!folder.exists()) return true
        if(folder.isDirectory){
                folder.listFiles()?.forEach { file->
                    deleteFile(file)
                }
        }
        return folder.delete()
    }

    private fun deleteFile(file: File):Boolean{
        return try {
            if (file.isDirectory) {
            file.listFiles()?.forEach { child ->
                deleteFile(child)
            }
        }
        file.delete()
    } catch (e: Exception){
        e.printStackTrace()
        false
    }
    }


    fun savePdf(name: String, uri: Uri): Int{
        val fileName= "${name.formatQuizName()}.pdf"

        val folder= File(context.filesDir, "materials/${name.formatQuizName()}")
        if(!folder.exists()){
            folder.mkdirs()
        }
        val file= File(folder, fileName)
        if(file.exists()) {
            deleteFile(file)
        }
        return try{
            val fos= FileOutputStream(file)
            context.contentResolver.openInputStream(uri)?.use {fis->
                fis.copyTo(fos)
            }

            val pageCount= FileInputStream(file).use { fis->
                val parcelFileDescriptor= ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
                PdfRenderer(parcelFileDescriptor).use { renderer->
                    renderer.pageCount
                }
            }
            pageCount
        } catch (e: Exception){
            Log.d("Yeet", e.stackTraceToString())
            -1
        }
    }

    fun saveMaterialJson(material: StudyMaterial){
        val folderName= if(material.id== 0L)
            material.name.formatQuizName()
        else updateFolderName(quizId = material.id, rawQuizName = material.name)
        val materialJsonString= Json.encodeToString(StudyMaterial.serializer() ,material)
        Log.d("Yeet" , "Save Material Json: $materialJsonString")
        val folder= File(context.filesDir, "/materials/$folderName")
        if(!folder.exists()){
            folder.mkdirs()
        }
        val jsonFile= File(folder, "material.json")
        if(jsonFile.exists()){
            if(!deleteFile(jsonFile)){
                throw IOException("Failed to delete existing file before replacing")
            }
        }
        jsonFile.writeText(materialJsonString)
    }

    fun prefixFolderWithMaterialId(name: String, id:Long){
        val folderName= name.formatQuizName()
        val folder= File(context.filesDir, "materials/$folderName")
        if(!folder.exists() || !folder.isDirectory) return

        val files= folder.listFiles()?: return
        for(file in files){
            if(file.isFile && file.name!= "material.json"){
                val newFileName= "${id}_${file.name}"
                val updatedFile= File(folder, newFileName)

                if(!file.renameTo(updatedFile)) throw IOException("File couldn't be renamed")
            }
        }
        appendFolderWithMaterialId(name, id)
    }

    fun appendFolderWithMaterialId(name: String, id: Long){
        val folderName= name.formatQuizName()
        val folder= File(context.filesDir, "materials/$folderName")
        if(!folder.exists() || !folder.isDirectory) return
        val parentFolder= File(context.filesDir, "materials")
        val newFolderName= "${id}-${folderName}"
        val updatedFolder= File(parentFolder, newFolderName)
        Log.d("Yeet", newFolderName)
        if(!folder.renameTo(updatedFolder)) throw IOException("Folder couldn't be renamed")
    }

    fun zipMaterialFolder(name: String, id: Long): Boolean{
        var status: Boolean
        val folderName= "$id-${name.formatQuizName()}"
        val folderToZip= File("${context.filesDir}/materials/$folderName")
        val zipFolder= File(context.filesDir, "material_zips")
        if(!zipFolder.exists()){
            zipFolder.mkdir()
        }
        val zipFile= File(zipFolder, "$folderName.zip")
        if(zipFile.exists()) deleteFile(zipFile)
        ZipOutputStream(BufferedOutputStream(FileOutputStream(zipFile))).use { outputStream->
            status = zipFiles(folderToZip, "", outputStream)
        }
        return status
    }

    fun saveTempMaterialZipFile(fileInfo: FileInfo):Boolean{
        return try{
            val extension= MimeTypeMap.getSingleton().getExtensionFromMimeType(fileInfo.mimeType)
            val fullFileName= "${fileInfo.name}.$extension"

            val downloadFolder= File(context.filesDir, "materials")
            if(!downloadFolder.exists()) {
                downloadFolder.mkdirs()
            }
            val zipFile= File(downloadFolder, fullFileName)
            if(zipFile.exists()){
                true
            }else{
                BufferedOutputStream(FileOutputStream(zipFile)).use{outputStream->
                    outputStream.write(fileInfo.bytes)
                }
                true
            }
        } catch (e:Exception){
            Log.d("Yeet", e.stackTraceToString())
            false
        }
    }

    fun unzipMaterialFiles(fileInfo: FileInfo): Boolean{
        val extension= MimeTypeMap.getSingleton().getExtensionFromMimeType(fileInfo.mimeType)
        val fullFileName= "${fileInfo.name}.$extension"
        val folder= File(context.filesDir, "materials")
        if(!folder.exists()) {
            throw Exception("Folder not detected for zip file")
        }
        //if folder already exists, no need to unzip again
        val resultFolder= File(context.filesDir, fileInfo.name)
        if(resultFolder.exists()) return true

        val zipFile= File(folder, fullFileName)
        if(!zipFile.exists() || !zipFile.isFile) return false

        return try {
            ZipInputStream(FileInputStream(zipFile)).use {inputStream->
                var entry = inputStream.nextEntry
                while(entry!=null){
                    val outputFile= File(folder, entry.name)
                    if(entry.isDirectory){
                        outputFile.mkdirs()
                    } else{
                        outputFile.parentFile?.mkdirs()
                        BufferedOutputStream(FileOutputStream(outputFile)).use {outputStream->
                            inputStream.copyTo(outputStream)
                        }
                    }
                    inputStream.closeEntry()
                    entry= inputStream.nextEntry
                }
            }
            deleteFile(zipFile)
            true
        } catch (e:Exception){
            Log.d("Yeet", "In UnzipFunction For Materials: ${e.stackTraceToString()}")
            false
        }
    }

    fun getMaterialFileInfo(id: Long, name: String): FileInfo{
        val fileName= "$id-${name.formatQuizName()}"
        val fullFileName= "${fileName}.zip"

        val folder= File(context.filesDir, "material_zips")
        if(!folder.exists() || !folder.isDirectory) throw IOException("Folder doesn't exist")

        val zipFile= File(folder, fullFileName)
        if(!zipFile.exists() || !zipFile.isFile) throw IOException("The zip file couldn't be created.")

        val mimeType= MimeTypeMap.getSingleton().getMimeTypeFromExtension(zipFile.extension)?:""
        val bytes= BufferedInputStream(FileInputStream(zipFile)).use {inputStream->
            inputStream.readBytes()
        }

        return FileInfo(
            name = fileName,
            mimeType = mimeType,
            bytes = bytes
        )
    }

    fun deleteMaterialFolderContents(name: String, id: Long): Boolean{
        val folderName= "$id-${name.formatQuizName()}"
        val folder= File(context.filesDir, "materials/${folderName}")
        Log.d("Yeet", folder.absolutePath)
        if(!folder.exists()) return true
        if(folder.isDirectory){
            folder.listFiles()?.forEach { file->
                deleteFile(file)
            }
        }
        return folder.delete()
    }




}
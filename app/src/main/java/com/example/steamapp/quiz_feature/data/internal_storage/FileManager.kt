package com.example.steamapp.quiz_feature.data.internal_storage

import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import com.example.steamapp.core.util.formatQuizName
import com.example.steamapp.quiz_feature.data.local.entities.relations.QuizWithQuestions
import kotlinx.serialization.json.Json
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class FileManager(
    private val context: Context
){
    fun saveImageOrAudio(uri: Uri, rawQuizName: String, questionId: Long){
        val quizName= rawQuizName.formatQuizName()
        val bytes= context.contentResolver.openInputStream(uri)?.use{ inputStream->
            inputStream.readBytes()
        }?: byteArrayOf()

        val mimeType= context.contentResolver.getType(uri)?:""
        val name= when{
            mimeType.startsWith("image/")-> "${questionId}_image"
            mimeType.startsWith("audio/")-> "${questionId}_audio"
            else-> ""
        }
        val extension= MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType)
        val fullFileName= "${name}.${extension}"
        val quizFolder= File(context.filesDir, "/quizzes/$quizName")
        if(!quizFolder.exists()){
            quizFolder.mkdirs()
        }
        val file= File(quizFolder , fullFileName)
        FileOutputStream(file).use { outputStream->
            outputStream.write(bytes)
        }
    }

    fun saveJson(quizWithQuestions: QuizWithQuestions){
        val quizName= quizWithQuestions.quiz.title.formatQuizName()
        val quizJsonString= Json.encodeToString(QuizWithQuestions.serializer() ,quizWithQuestions)
        val folder= File(context.filesDir, "/quizzes/${quizName}")
        if(!folder.exists()){
            folder.mkdirs()
        }
        val jsonFile= File(folder, "questions.json")
        jsonFile.writeText(quizJsonString)
    }

    fun zipFolder(rawQuizName: String){
        val quizName= rawQuizName.formatQuizName()
        val folderToZip= File("${context.filesDir}/quizzes/$quizName")
        val zipFolder= File(context.filesDir, "/zips")
        if(!zipFolder.exists()){
            zipFolder.mkdir()
        }
        val zipFile= File(zipFolder, "{$quizName}.zip")
        ZipOutputStream(BufferedOutputStream(FileOutputStream(zipFile))).use { outputStream->
            zipFiles(folderToZip, "", outputStream)
        }
    }

    private fun zipFiles(fileToZip: File, parentDirPath: String, zipOutputStream: ZipOutputStream) {
        if (fileToZip.isHidden) return

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
    }

    fun deleteFolderContents(rawQuizName: String): Boolean{
        val quizName= rawQuizName.formatQuizName()
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
        if(file.isDirectory){
            file.listFiles()?.forEach { child->
                deleteFile(child)
            }
        }
        return file.delete()
    }
}
package com.example.steamapp.core.util

fun transformFilePath(filePath: String?, quizId: Long): String? {
    if(filePath.isNullOrBlank())
        return null
    // Split the path into directories and file name
    val parts = filePath.split("/")
    if (parts.size < 3) throw IllegalArgumentException("Invalid file path format")

    // Extract the parent directory and file name
    val parentDir = parts[parts.size - 2]
    val fileName = parts.last()

    // Transform the parent directory and file name
    val newParentDir = "$quizId-$parentDir"
    val newFileName = "${quizId}_$fileName"

    // Rebuild the path with the transformed parts
    return (parts.dropLast(2) + newParentDir + newFileName).joinToString("/")
}
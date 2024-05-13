package com.video.app.config

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object ValidRegex {
    private val emailPattern = Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
    private val phonePattern = Regex("^(\\+[0-9]{1,3})?[0-9]{10,}$")
    fun isEmail(value: String): Boolean {
        return emailPattern.matches(value)
    }

    fun isPhone(value: String): Boolean {
        return phonePattern.matches(value)
    }
}

fun getFileFromUri(context: Context, uri: Uri): File? {
    val contentResolver: ContentResolver = context.contentResolver
    val file = File(context.cacheDir, "temp_file")

    try {
        val inputStream = contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        inputStream?.copyTo(outputStream)
        inputStream?.close()
        outputStream.close()
        return file
    } catch (e: IOException) {
        e.printStackTrace()
        return null
    }
}
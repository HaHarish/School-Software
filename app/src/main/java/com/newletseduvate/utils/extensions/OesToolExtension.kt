package com.newletseduvate.utils.extensions

import android.content.Context
import android.util.Log
import android.webkit.MimeTypeMap
import com.newletseduvate.model.general.UploadFileModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.util.*

fun Context.prepareFilePart(
    partName: String,
    fileUri: String
): UploadFileModel {
    val file = File(fileUri)
    val requestFile = file.asRequestBody(getMimeType(fileUri)?.toMediaTypeOrNull())
    return UploadFileModel(file, requestFile)
}

fun Context.getMimeType(url: String): String? {
    try {
        var type: String? = null
        val extension = url.substring(url.lastIndexOf(".") + 1, url.length)
        Log.i("extension", "ext : $extension")
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                extension.toLowerCase(
                    Locale.getDefault()
                )
            )
        }
        return type
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}
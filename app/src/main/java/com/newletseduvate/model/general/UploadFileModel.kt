package com.newletseduvate.model.general

import okhttp3.RequestBody
import java.io.File

data class UploadFileModel(val file: File, val requestBody: RequestBody)
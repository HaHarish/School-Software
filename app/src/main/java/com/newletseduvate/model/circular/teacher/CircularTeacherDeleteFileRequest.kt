package com.newletseduvate.model.circular.teacher


import com.google.gson.annotations.SerializedName

data class CircularTeacherDeleteFileRequest(
    @SerializedName("file_name")
    var fileName: String?
)
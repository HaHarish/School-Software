package com.newletseduvate.model.circular.teacher


import com.google.gson.annotations.SerializedName

data class CircularTeacherDeleteFileResponse(
    @SerializedName("status_code")
    var statusCode: Int?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("data")
    var `data`: Data?
) {
    class Data(
    )
}
package com.newletseduvate.model.online_class


import com.google.gson.annotations.SerializedName

data class StudentAttendOnlineClassMarkAttendanceResponse(
    @SerializedName("status_code")
    val statusCode: Int,
    @SerializedName("status")
    val status: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val `data`: StudentAttendOnlineClassMarkAttendanceModel?
)
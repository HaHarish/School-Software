package com.newletseduvate.model.online_class


import com.google.gson.annotations.SerializedName

data class OnlineClassStudentOcDetailsResponse(
    @SerializedName("status_code")
    val statusCode: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val `data`: ArrayList<OnlineClassStudentOcDetailsModel>?
)
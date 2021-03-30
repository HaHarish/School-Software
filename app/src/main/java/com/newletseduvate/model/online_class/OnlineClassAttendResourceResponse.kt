package com.newletseduvate.model.online_class


import com.google.gson.annotations.SerializedName

data class OnlineClassAttendResourceResponse(
    @SerializedName("status_code")
    var statusCode: Int?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("result")
    var result: ArrayList<OnlineClassAttendResourceModel>?
)
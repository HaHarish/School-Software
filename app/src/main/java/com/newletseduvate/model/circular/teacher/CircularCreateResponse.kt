package com.newletseduvate.model.circular.teacher


import com.google.gson.annotations.SerializedName

data class CircularCreateResponse(
    @SerializedName("status_code")
    var statusCode: Int?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("result")
    var result: List<Any?>?
)
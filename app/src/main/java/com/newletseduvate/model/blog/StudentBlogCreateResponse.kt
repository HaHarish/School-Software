package com.newletseduvate.model.blog


import com.google.gson.annotations.SerializedName

data class StudentBlogCreateResponse(
    @SerializedName("status_code")
    val statusCode: Int?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("result")
    val result: Int?
)
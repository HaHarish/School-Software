package com.newletseduvate.model.blog


import com.google.gson.annotations.SerializedName

data class StudentBlogGenreResponse(
    @SerializedName("status_code")
    val statusCode: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("result")
    val result: ArrayList<StudentBlogGenreModel>
)
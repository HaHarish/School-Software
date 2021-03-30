package com.newletseduvate.model.blog

import com.google.gson.annotations.SerializedName

data class StudentBlogResponse(
    @SerializedName("status_code")
    val statusCode: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("result")
    val result: Result?
) {
    data class Result(
        @SerializedName("total_blogs")
        val totalBlogs: Int,
        @SerializedName("data")
        val `data`: ArrayList<StudentBlogModel>,
        @SerializedName("total_pages")
        val totalPages: Int
    )
}
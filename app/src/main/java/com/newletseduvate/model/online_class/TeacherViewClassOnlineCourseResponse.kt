package com.newletseduvate.model.online_class


import com.google.gson.annotations.SerializedName

data class TeacherViewClassOnlineCourseResponse(
    @SerializedName("next")
    val next: Any?,
    @SerializedName("previous")
    val previous: Any?,
    @SerializedName("count")
    val count: Int?,
    @SerializedName("limit")
    val limit: Int?,
    @SerializedName("current_page")
    val currentPage: Int?,
    @SerializedName("total_pages")
    val totalPages: Int?,
    @SerializedName("result")
    val result: ArrayList<CourseModel>?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("status_code")
    val statusCode: Int?
)
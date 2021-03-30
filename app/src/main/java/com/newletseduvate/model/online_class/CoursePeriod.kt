package com.newletseduvate.model.online_class

import com.google.gson.annotations.SerializedName

data class CoursePeriod(
    @SerializedName("id")
    var id: Int?,
    @SerializedName("course_id")
    var courseId: Int?,
    @SerializedName("title")
    var title: String?,
    @SerializedName("description")
    var description: String?,
    @SerializedName("files")
    var files: List<Any?>?
)
package com.newletseduvate.model.lesson_plan.teacher


import com.google.gson.annotations.SerializedName

data class TeacherLessonPlanFilterResultsResponse(
    @SerializedName("next")
    var next: Any?,
    @SerializedName("previous")
    var previous: Any?,
    @SerializedName("count")
    var count: Int?,
    @SerializedName("limit")
    var limit: Int?,
    @SerializedName("current_page")
    var currentPage: Int?,
    @SerializedName("total_pages")
    var totalPages: Int?,
    @SerializedName("result")
    var result: ArrayList<Result>?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("status_code")
    var statusCode: Int?
) {
    data class Result(
        @SerializedName("id")
        var id: Int?,
        @SerializedName("period_name")
        var periodName: String?,
        @SerializedName("chapter_name")
        var chapterName: String?,
        @SerializedName("updated_at")
        var updatedAt: String?
    )
}
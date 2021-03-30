package com.newletseduvate.model.lesson_plan


import com.google.gson.annotations.SerializedName

data class LessonPlanGradeResponse(
    @SerializedName("status_code")
    var statusCode: Int?,
    @SerializedName("status")
    var status: String?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("data")
    var `data`: List<Data?>?
) {
    data class Data(
        @SerializedName("id")
        var id: Int?,
        @SerializedName("grade_id")
        var gradeId: Int?,
        @SerializedName("grade__grade_name")
        var gradeGradeName: String?,
        @SerializedName("created_at")
        var createdAt: String?,
        @SerializedName("created_by")
        var createdBy: Any?
    )
}
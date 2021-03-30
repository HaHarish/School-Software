package com.newletseduvate.model.lesson_plan


import com.google.gson.annotations.SerializedName

data class LessonPlanSubjectResponse(
    @SerializedName("status_code")
    var statusCode: Int?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("result")
    var result: List<Result?>?
) {
    data class Result(
        @SerializedName("grade")
        var grade: Int?,
        @SerializedName("id")
        var id: Int?,
        @SerializedName("subject_id")
        var subjectId: Int?,
        @SerializedName("subject_name")
        var subjectName: String?
    )
}
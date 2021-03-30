package com.newletseduvate.model.lesson_plan.teacher


import com.google.gson.annotations.SerializedName

data class TeacherLessonPlanViewMoreResponse(
    @SerializedName("status_code")
    var statusCode: Int?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("result")
    var result: ArrayList<Result>?
) {
    data class Result(
        @SerializedName("id")
        var id: Int?,
        @SerializedName("document_type")
        var documentType: String?,
        @SerializedName("media_file")
        var mediaFile: ArrayList<String>?,
        @SerializedName("mapping_id")
        var mappingId: Int?
    )
}
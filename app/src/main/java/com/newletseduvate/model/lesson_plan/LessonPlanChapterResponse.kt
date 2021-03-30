package com.newletseduvate.model.lesson_plan


import com.google.gson.annotations.SerializedName

data class LessonPlanChapterResponse(
    @SerializedName("status_code")
    var statusCode: Int?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("result")
    var result: Result?
) {
    data class Result(
        @SerializedName("chapter_list")
        var chapterList: List<Chapter?>?,
        @SerializedName("central_gs_mapping_id")
        var centralGsMappingId: Int?,
        @SerializedName("central_subject_name")
        var centralSubjectName: String?,
        @SerializedName("central_grade_name")
        var centralGradeName: String?
    ) {
        data class Chapter(
            @SerializedName("id")
            var id: Int?,
            @SerializedName("chapter_name")
            var chapterName: String?,
            @SerializedName("no_of_periods")
            var noOfPeriods: Int?
        )
    }
}
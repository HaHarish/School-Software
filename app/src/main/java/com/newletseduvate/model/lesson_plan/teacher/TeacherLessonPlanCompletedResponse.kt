package com.newletseduvate.model.lesson_plan.teacher


import com.google.gson.annotations.SerializedName

data class TeacherLessonPlanCompletedResponse(
    @SerializedName("status_code")
    var statusCode: Int?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("result")
    var result: Result?
) {
    data class Result(
        @SerializedName("id")
        var id: Int?,
        @SerializedName("academic_year")
        var academicYear: String?,
        @SerializedName("academic_year_id")
        var academicYearId: Int?,
        @SerializedName("volume_id")
        var volumeId: Int?,
        @SerializedName("volume_name")
        var volumeName: String?,
        @SerializedName("chapter_id")
        var chapterId: Int?,
        @SerializedName("chapter_name")
        var chapterName: String?,
        @SerializedName("central_gs_mapping_id")
        var centralGsMappingId: Int?,
        @SerializedName("period_id")
        var periodId: Int?,
        @SerializedName("start_at")
        var startAt: Any?,
        @SerializedName("completed_at")
        var completedAt: String?,
        @SerializedName("is_completed")
        var isCompleted: Boolean?,
        @SerializedName("completed_by")
        var completedBy: Int?,
        @SerializedName("grade_subject")
        var gradeSubject: Any?,
        @SerializedName("subject_id")
        var subjectId: Int?
    )
}
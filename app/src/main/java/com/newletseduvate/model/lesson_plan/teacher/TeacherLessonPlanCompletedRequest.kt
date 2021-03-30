package com.newletseduvate.model.lesson_plan.teacher


import com.google.gson.annotations.SerializedName

data class TeacherLessonPlanCompletedRequest(
    @SerializedName("academic_year")
    var academicYear: String?,
    @SerializedName("academic_year_id")
    var academicYearId: Int?,
    @SerializedName("volume_id")
    var volumeId: Int?,
    @SerializedName("volume_name")
    var volumeName: String?,
    @SerializedName("subject_id")
    var subjectId: Int?,
    @SerializedName("chapter_id")
    var chapterId: Int?,
    @SerializedName("chapter_name")
    var chapterName: String?,
    @SerializedName("central_gs_mapping_id")
    var centralGsMappingId: Int?,
    @SerializedName("period_id")
    var periodId: Int?
)
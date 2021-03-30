package com.newletseduvate.model.diary.teacher


import com.google.gson.annotations.SerializedName

data class TeacherDiaryCreateDailyResponse(
    @SerializedName("status_code")
    var statusCode: Int?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("result")
    var result: Result?
) {
    data class Result(
        @SerializedName("academic_year")
        var academicYear: Int?,
        @SerializedName("branch")
        var branch: Int?,
        @SerializedName("grade")
        var grade: List<Int?>?,
        @SerializedName("section_mapping")
        var sectionMapping: List<Any?>?,
        @SerializedName("subject")
        var subject: Int?,
        @SerializedName("chapter")
        var chapter: Int?,
        @SerializedName("dairy_type")
        var dairyType: String?,
        @SerializedName("teacher_report")
        var teacherReport: TeacherReport?,
        @SerializedName("title")
        var title: String?,
        @SerializedName("message")
        var message: String?,
        @SerializedName("documents")
        var documents: List<String?>?,
        @SerializedName("created_by")
        var createdBy: Int?,
        @SerializedName("created_at")
        var createdAt: String?
    ) {
        data class TeacherReport(
            @SerializedName("previous_class")
            var previousClass: String?,
            @SerializedName("summary")
            var summary: String?,
            @SerializedName("class_work")
            var classWork: String?,
            @SerializedName("tools_used")
            var toolsUsed: String?,
            @SerializedName("homework")
            var homework: String?
        )
    }
}
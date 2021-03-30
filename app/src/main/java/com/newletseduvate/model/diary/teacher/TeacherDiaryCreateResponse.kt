package com.newletseduvate.model.diary.teacher


import com.google.gson.annotations.SerializedName

data class TeacherDiaryCreateResponse(
    @SerializedName("status_code")
    var statusCode: Int?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("result")
    var result: Result?
) {
    data class Result(
        @SerializedName("academic_year")
        var academicYear: Any?,
        @SerializedName("branch")
        var branch: Int?,
        @SerializedName("grade")
        var grade: List<Int?>?,
        @SerializedName("section_mapping")
        var sectionMapping: List<Any?>?,
        @SerializedName("subject")
        var subject: Any?,
        @SerializedName("chapter")
        var chapter: Any?,
        @SerializedName("dairy_type")
        var dairyType: String?,
        @SerializedName("teacher_report")
        var teacherReport: Any?,
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
    )
}
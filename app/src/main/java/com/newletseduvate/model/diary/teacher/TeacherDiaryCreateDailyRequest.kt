package com.newletseduvate.model.diary.teacher


import com.google.gson.annotations.SerializedName

data class TeacherDiaryCreateDailyRequest(
    @SerializedName("academic_year")
    var academicYear: Int?,
    @SerializedName("module_id")
    var moduleId: Int?,
    @SerializedName("branch")
    var branch: Int?,
    @SerializedName("grade")
    var grade: List<Int?>?,
    @SerializedName("section")
    var section: List<Int?>?,
    @SerializedName("subject")
    var subject: Int?,
    @SerializedName("chapter")
    var chapter: Int?,
    @SerializedName("documents")
    var documents: List<String?>?,
    @SerializedName("teacher_report")
    var teacherReport: TeacherReport?,
    @SerializedName("dairy_type")
    var dairyType: Int?
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
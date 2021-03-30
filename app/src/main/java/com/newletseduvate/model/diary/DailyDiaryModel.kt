package com.newletseduvate.model.diary


import com.google.gson.annotations.SerializedName

data class DailyDiaryModel(
    @SerializedName("academic_year")
    var academicYear: Any?,
    @SerializedName("branch")
    var branch: Branch,
    @SerializedName("chapter")
    var chapter: Any?,
    @SerializedName("created_at")
    var createdAt: String,
    @SerializedName("created_by")
    var createdBy: CreatedBy,
    @SerializedName("dairy_type")
    var dairyType: String,
    @SerializedName("documents")
    var documents: ArrayList<String>,
    @SerializedName("erp_users")
    var erpUsers: ArrayList<Any>,
    @SerializedName("grade")
    var grade: ArrayList<Grade>,
    @SerializedName("id")
    var id: Int,
    @SerializedName("message")
    var message: String,
    @SerializedName("section")
    var section: ArrayList<Section>,
    @SerializedName("subject")
    var subject: Subject,
    @SerializedName("teacher_report")
    var teacherReport: TeacherReport,
    @SerializedName("title")
    var title: String
) {
    data class Branch(
        @SerializedName("branch_name")
        var branchName: String,
        @SerializedName("id")
        var id: Int
    )

    data class CreatedBy(
        @SerializedName("first_name")
        var firstName: String,
        @SerializedName("id")
        var id: Int,
        @SerializedName("last_name")
        var lastName: String
    )

    class Grade(
        @SerializedName("grade_name")
        var gradeName: String?,
        @SerializedName("id")
        var id: Int?
    )

    data class Section(
        @SerializedName("section_name")
        var sectionName: Any?,
        @SerializedName("section_slag")
        var sectionSlag: Any?
    )

    data class Subject(
        @SerializedName("id")
        var id: Int,
        @SerializedName("subject_name")
        var subjectName: String
    )

    data class TeacherReport(
        @SerializedName("class_work")
        var classWork: String,
        @SerializedName("homework")
        var homework: String,
        @SerializedName("previous_class")
        var previousClass: String,
        @SerializedName("summary")
        var summary: String,
        @SerializedName("tools_used")
        var toolsUsed: String
    )
}
package com.newletseduvate.model.circular.teacher


import com.google.gson.annotations.SerializedName
import com.newletseduvate.model.circular.student.CircularResultModel

data class CircularFilterResponse(
    @SerializedName("next")
    var next: String?,
    @SerializedName("previous")
    var previous: Any?,
    @SerializedName("count")
    var count: Int?,
    @SerializedName("limit")
    var limit: Int?,
    @SerializedName("current_page")
    var currentPage: Int?,
    @SerializedName("total_pages")
    var totalPages: Int?,
    @SerializedName("result")
    var result: ArrayList<CircularResultModel>?,
    @SerializedName("massage")
    var massage: String?,
    @SerializedName("status_code")
    var statusCode: Int?
) /*{
    data class Result(
        @SerializedName("id")
        var id: Int?,
        @SerializedName("circular_name")
        var circularName: String?,
        @SerializedName("academic_year")
        var academicYear: AcademicYear?,
        @SerializedName("description")
        var description: String?,
        @SerializedName("uploaded_by")
        var uploadedBy: UploadedBy?,
        @SerializedName("branches")
        var branches: List<Branche?>?,
        @SerializedName("grades")
        var grades: List<Grade?>?,
        @SerializedName("sections")
        var sections: List<Section?>?,
        @SerializedName("module_name")
        var moduleName: String?,
        @SerializedName("media")
        var media: List<String?>?,
        @SerializedName("upload_for")
        var uploadFor: String?,
        @SerializedName("is_delete")
        var isDelete: Boolean?,
        @SerializedName("time_stamp")
        var timeStamp: String?
    ) {
        data class AcademicYear(
            @SerializedName("id")
            var id: Int?,
            @SerializedName("session_year")
            var sessionYear: String?
        )

        data class UploadedBy(
            @SerializedName("id")
            var id: Int?,
            @SerializedName("first_name")
            var firstName: String?
        )

        data class Branche(
            @SerializedName("id")
            var id: Int?,
            @SerializedName("branch_name")
            var branchName: String?
        )

        data class Grade(
            @SerializedName("id")
            var id: Int?,
            @SerializedName("grade_name")
            var gradeName: String?
        )

        data class Section(
            @SerializedName("id")
            var id: Int?,
            @SerializedName("section_name")
            var sectionName: String?,
            @SerializedName("section_slag")
            var sectionSlag: String?
        )
    }
}*/
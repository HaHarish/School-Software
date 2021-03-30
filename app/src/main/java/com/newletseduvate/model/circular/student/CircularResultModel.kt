package com.newletseduvate.model.circular.student

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CircularResultModel (
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
    var media: ArrayList<String>?,
    @SerializedName("upload_for")
    var uploadFor: String?,
    @SerializedName("is_delete")
    var isDelete: Boolean?,
    @SerializedName("time_stamp")
    var timeStamp: String?
): Serializable {
    data class AcademicYear(
        @SerializedName("id")
        var id: Int?,
        @SerializedName("session_year")
        var sessionYear: String?
    ): Serializable

    data class UploadedBy(
        @SerializedName("id")
        var id: Int?,
        @SerializedName("first_name")
        var firstName: String?
    ): Serializable

    data class Branche(
        @SerializedName("id")
        var id: Int?,
        @SerializedName("branch_name")
        var branchName: String?
    ): Serializable

    data class Grade(
        @SerializedName("id")
        var id: Int?,
        @SerializedName("grade_name")
        var gradeName: String?
    ): Serializable

    data class Section(
        @SerializedName("id")
        var id: Int?,
        @SerializedName("section_name")
        var sectionName: String?,
        @SerializedName("section_slag")
        var sectionSlag: String?
    ): Serializable
}
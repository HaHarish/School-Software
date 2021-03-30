package com.newletseduvate.model.profile


import com.google.gson.annotations.SerializedName

data class ProfileDetailsResponse(
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
        @SerializedName("erp_id")
        var erpId: String?,
        @SerializedName("user_middle_name")
        var userMiddleName: String?,
        @SerializedName("contact")
        var contact: String?,
        @SerializedName("gender")
        var gender: String?,
        @SerializedName("is_active")
        var isActive: Boolean?,
        @SerializedName("subjects")
        var subjects: List<Subject?>?,
        @SerializedName("profile")
        var profile: Any?,
        @SerializedName("user")
        var user: User?,
        @SerializedName("parent_details")
        var parentDetails: ParentDetails?,
        @SerializedName("mapping_bgs")
        var mappingBgs: List<MappingBg?>?,
        @SerializedName("status")
        var status: String?,
        @SerializedName("academic_year")
        var academicYear: AcademicYear?,
        @SerializedName("is_delete")
        var isDelete: Boolean?,
        @SerializedName("address")
        var address: String?,
        @SerializedName("date_of_birth")
        var dateOfBirth: String?,
        @SerializedName("name")
        var name: String?,
        @SerializedName("branch_id")
        var branchId: BranchId?
    ) {
        data class Subject(
            @SerializedName("id")
            var id: Int?,
            @SerializedName("subject_name")
            var subjectName: String?,
            @SerializedName("subject_slag")
            var subjectSlag: String?,
            @SerializedName("subject_description")
            var subjectDescription: String?,
            @SerializedName("is_optional")
            var isOptional: Boolean?,
            @SerializedName("is_delete")
            var isDelete: Boolean?,
            @SerializedName("created_at")
            var createdAt: String?,
            @SerializedName("updated_at")
            var updatedAt: String?,
            @SerializedName("created_by")
            var createdBy: Int?,
            @SerializedName("updated_by")
            var updatedBy: Any?
        )

        data class User(
            @SerializedName("id")
            var id: Int?,
            @SerializedName("username")
            var username: String?,
            @SerializedName("first_name")
            var firstName: String?,
            @SerializedName("last_name")
            var lastName: String?,
            @SerializedName("email")
            var email: String?
        )

        class ParentDetails(
        )

        data class MappingBg(
            @SerializedName("section")
            var section: List<Section?>?,
            @SerializedName("grade")
            var grade: List<Grade?>?,
            @SerializedName("branch")
            var branch: List<Branch?>?
        ) {
            data class Section(
                @SerializedName("section_id")
                var sectionId: Int?,
                @SerializedName("section__section_name")
                var sectionSectionName: String?
            )

            data class Grade(
                @SerializedName("grade_id")
                var gradeId: Int?,
                @SerializedName("grade__grade_name")
                var gradeGradeName: String?
            )

            data class Branch(
                @SerializedName("branch_id")
                var branchId: Int?,
                @SerializedName("branch__branch_name")
                var branchBranchName: String?
            )
        }

        data class AcademicYear(
            @SerializedName("id")
            var id: Int?,
            @SerializedName("session_year")
            var sessionYear: String?,
            @SerializedName("is_delete")
            var isDelete: Boolean?,
            @SerializedName("start_date")
            var startDate: Any?,
            @SerializedName("end_date")
            var endDate: Any?
        )

        data class BranchId(
            @SerializedName("id")
            var id: Int?,
            @SerializedName("branch_name")
            var branchName: String?,
            @SerializedName("logo")
            var logo: String?
        )
    }
}
package com.newletseduvate.model.diary.teacher


import com.google.gson.annotations.SerializedName

data class TeacherDiaryActiveStudentsResponse(
    @SerializedName("status_code")
    var statusCode: Int?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("result")
    var result: Result?
) {
    data class Result(
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
        @SerializedName("results")
        var results: ArrayList<NameResults>?
    ) {
        data class NameResults(
            @SerializedName("id")
            var id: Int?,
            @SerializedName("name")
            var name: String?,
            @SerializedName("erp_id")
            var erpId: String?,
            @SerializedName("academic_year")
            var academicYear: AcademicYear?,
            @SerializedName("section_mapping")
            var sectionMapping: List<SectionMapping?>?
        ) {
            data class AcademicYear(
                @SerializedName("id")
                var id: Int?,
                @SerializedName("session_year")
                var sessionYear: Int?
            )

            data class SectionMapping(
                @SerializedName("id")
                var id: Int?,
                @SerializedName("acad_session")
                var acadSession: AcadSession?,
                @SerializedName("grade")
                var grade: Grade?,
                @SerializedName("section")
                var section: Section?
            ) {
                data class AcadSession(
                    @SerializedName("id")
                    var id: Int?,
                    @SerializedName("session_year")
                    var sessionYear: Int?
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
                    var sectionSlag: Any?
                )
            }
        }
    }
}
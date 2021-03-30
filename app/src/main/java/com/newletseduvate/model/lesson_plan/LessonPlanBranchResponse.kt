package com.newletseduvate.model.lesson_plan


import com.google.gson.annotations.SerializedName

data class LessonPlanBranchResponse(
    @SerializedName("status_code")
    var statusCode: Int?,
    @SerializedName("status")
    var status: String?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("data")
    var `data`: Data?
) {
    data class Data(
        @SerializedName("count")
        var count: Int?,
        @SerializedName("next")
        var next: Any?,
        @SerializedName("previous")
        var previous: Any?,
        @SerializedName("results")
        var results: List<Result?>?
    ) {
        data class Result(
            @SerializedName("id")
            var id: Int?,
            @SerializedName("session_year")
            var sessionYear: SessionYear?,
            @SerializedName("branch")
            var branch: Branch?
        ) {
            data class SessionYear(
                @SerializedName("id")
                var id: Int?,
                @SerializedName("session_year")
                var sessionYear: String?
            )

            data class Branch(
                @SerializedName("id")
                var id: Int?,
                @SerializedName("created_by")
                var createdBy: String?,
                @SerializedName("branch_name")
                var branchName: String?,
                @SerializedName("branch_code")
                var branchCode: String?,
                @SerializedName("affiliation_code")
                var affiliationCode: Any?,
                @SerializedName("is_central")
                var isCentral: Boolean?,
                @SerializedName("is_school")
                var isSchool: Boolean?,
                @SerializedName("createdAt")
                var createdAt: String?,
                @SerializedName("updatedAt")
                var updatedAt: String?,
                @SerializedName("address")
                var address: String?,
                @SerializedName("logo")
                var logo: String?,
                @SerializedName("city")
                var city: Any?,
                @SerializedName("branch_enrollment_code")
                var branchEnrollmentCode: Any?,
                @SerializedName("branch_cbse_affiliation_number")
                var branchCbseAffiliationNumber: Any?,
                @SerializedName("is_internal")
                var isInternal: Boolean?,
                @SerializedName("sms_sender_id")
                var smsSenderId: Any?,
                @SerializedName("is_delete")
                var isDelete: Boolean?,
                @SerializedName("updated_by")
                var updatedBy: Any?
            )
        }
    }
}
package com.newletseduvate.model.diary.teacher


import com.google.gson.annotations.SerializedName

data class TeacherDiaryBranchResponse(
    @SerializedName("status_code")
    var statusCode: Int?,
    @SerializedName("status")
    var status: String?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("data")
    var `data`: List<Data?>?
) {
    data class Data(
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
        @SerializedName("created_at")
        var createdAt: String?,
        @SerializedName("updated_at")
        var updatedAt: String?,
        @SerializedName("updated_by")
        var updatedBy: Any?
    )
}
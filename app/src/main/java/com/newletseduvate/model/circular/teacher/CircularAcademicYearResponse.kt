package com.newletseduvate.model.circular.teacher


import com.google.gson.annotations.SerializedName

data class CircularAcademicYearResponse(
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
        @SerializedName("is_delete")
        var isDelete: Boolean?,
        @SerializedName("session_year")
        var sessionYear: String?,
        @SerializedName("branch")
        var branch: Any?,
        @SerializedName("created_by")
        var createdBy: Any?
    )
}
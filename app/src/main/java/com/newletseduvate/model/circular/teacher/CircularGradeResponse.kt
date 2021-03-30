package com.newletseduvate.model.circular.teacher


import com.google.gson.annotations.SerializedName

data class CircularGradeResponse(
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
        @SerializedName("grade_id")
        var gradeId: Int?,
        @SerializedName("grade__grade_name")
        var gradeGradeName: String?
    )
}
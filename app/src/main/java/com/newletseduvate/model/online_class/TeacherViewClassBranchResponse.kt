package com.newletseduvate.model.online_class


import com.google.gson.annotations.SerializedName

data class TeacherViewClassBranchResponse(
    @SerializedName("status_code")
    val statusCode: Int?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("data")
    val `data`: Data?
) {
    data class Data(
        @SerializedName("count")
        val count: Int?,
        @SerializedName("next")
        val next: Any?,
        @SerializedName("previous")
        val previous: Any?,
        @SerializedName("results")
        val results: ArrayList<Result?>?
    ) {
        data class Result(
            @SerializedName("id")
            val id: Int?,
            @SerializedName("session_year")
            val sessionYear: SessionYear?,
            @SerializedName("branch")
            val branch: BranchModel?
        ) {
            data class SessionYear(
                @SerializedName("id")
                val id: Int?,
                @SerializedName("session_year")
                val sessionYear: String?
            )

        }
    }
}
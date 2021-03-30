package com.newletseduvate.model.diary


import com.google.gson.annotations.SerializedName

data class GeneralDiaryResponse(
    @SerializedName("message")
    var message: String,
    @SerializedName("result")
    var result: Result?,
    @SerializedName("status_code")
    var statusCode: Int
) {
    data class Result(
        @SerializedName("count")
        var count: Int,
        @SerializedName("current_page")
        var currentPage: Int,
        @SerializedName("limit")
        var limit: Int,
        @SerializedName("next")
        var next: Any?,
        @SerializedName("previous")
        var previous: Any?,
        @SerializedName("results")
        var results: ArrayList<GeneralDiaryModel>,
        @SerializedName("total_pages")
        var totalPages: Int
    )

}
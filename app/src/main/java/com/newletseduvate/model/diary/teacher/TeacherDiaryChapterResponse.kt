package com.newletseduvate.model.diary.teacher


import com.google.gson.annotations.SerializedName

data class TeacherDiaryChapterResponse(
    @SerializedName("status_code")
    var statusCode: Int?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("result")
    var result: List<Result?>?
) {
    data class Result(
        @SerializedName("id")
        var id: Int?,
        @SerializedName("chapter_name")
        var chapterName: String?
    )
}
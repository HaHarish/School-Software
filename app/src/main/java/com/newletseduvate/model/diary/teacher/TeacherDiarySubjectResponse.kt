package com.newletseduvate.model.diary.teacher


import com.google.gson.annotations.SerializedName

data class TeacherDiarySubjectResponse(
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
        @SerializedName("subject__id")
        var subjectId: Int?,
        @SerializedName("subject__subject_name")
        var subjectSubjectName: String?
    )
}
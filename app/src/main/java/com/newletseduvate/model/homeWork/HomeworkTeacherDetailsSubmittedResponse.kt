package com.newletseduvate.model.homeWork


import com.google.gson.annotations.SerializedName

data class HomeworkTeacherDetailsSubmittedResponse(
    @SerializedName("status_code")
    val statusCode: Int?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("data")
    val `data`: Data?
) {
    data class Data(
        @SerializedName("id")
        val id: Int?,
        @SerializedName("homework_name")
        val homeworkName: String?,
        @SerializedName("description")
        val description: String?,
        @SerializedName("hw_questions")
        val hwQuestions: ArrayList<HomeworkTeacherDetailsSubmittedModel>?
    )
}
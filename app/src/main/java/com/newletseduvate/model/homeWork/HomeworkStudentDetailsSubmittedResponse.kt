package com.newletseduvate.model.homeWork


import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName

data class HomeworkStudentDetailsSubmittedResponse(
    @SerializedName("status_code")
    val statusCode: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val `data`: Data?
) {
    data class Data(
        @SerializedName("id")
        val id: Int,
        @SerializedName("homework")
        val homework: Int,
        @SerializedName("is_question_wise")
        val isQuestionWise: Boolean,
        @SerializedName("hw_questions")
        val hwQuestions: JsonElement?
    )
}
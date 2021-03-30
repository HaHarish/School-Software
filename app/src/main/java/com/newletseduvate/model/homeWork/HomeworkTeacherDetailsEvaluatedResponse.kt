package com.newletseduvate.model.homeWork


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class HomeworkTeacherDetailsEvaluatedResponse(
    @SerializedName("status_code")
    val statusCode: Int?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("evaluated_list")
    val evaluatedList: ArrayList<Evaluated>?,
    @SerializedName("submitted_list")
    val submittedList: ArrayList<Submitted>?,
    @SerializedName("unevaluated_list")
    val unevaluatedList: ArrayList<Unevaluated>?,
    @SerializedName("un_submitted_list")
    val unSubmittedList: ArrayList<UnSubmitted>?
) {
    data class Evaluated(
        @SerializedName("student_homework_id")
        val studentHomeworkId: Int?,
        @SerializedName("submitted_by")
        val submittedBy: Int?,
        @SerializedName("hw_status")
        val hwStatus: String?,
        @SerializedName("first_name")
        val firstName: String?,
        @SerializedName("last_name")
        val lastName: String?
    ) : Serializable

    data class Submitted(
        @SerializedName("student_homework_id")
        val studentHomeworkId: Int?,
        @SerializedName("submitted_by")
        val submittedBy: Int?,
        @SerializedName("hw_status")
        val hwStatus: String?,
        @SerializedName("first_name")
        val firstName: String?,
        @SerializedName("last_name")
        val lastName: String?
    ) : Serializable

    data class Unevaluated(
        @SerializedName("student_homework_id")
        val studentHomeworkId: Int?,
        @SerializedName("submitted_by")
        val submittedBy: Int?,
        @SerializedName("hw_status")
        val hwStatus: String?,
        @SerializedName("first_name")
        val firstName: String?,
        @SerializedName("last_name")
        val lastName: String?
    )

    data class UnSubmitted(
        @SerializedName("status")
        val status: String?,
        @SerializedName("id")
        val id: Int?,
        @SerializedName("user_id")
        val userId: Int?,
        @SerializedName("first_name")
        val firstName: String?,
        @SerializedName("last_name")
        val lastName: String?
    )
}
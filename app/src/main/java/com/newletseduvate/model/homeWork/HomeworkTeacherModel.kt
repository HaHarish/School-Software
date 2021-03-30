package com.newletseduvate.model.homeWork

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class HomeworkTeacherModel(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("subject")
    val subject: Int?,
    @SerializedName("status")
    val status: Status?,
    var classDate: String?,
    var isAddEnabled: Boolean = false,
    var canUpload : Boolean = false
) : Serializable {
    data class Status(
        @SerializedName("student_submitted")
        val studentSubmitted: Int?,
        @SerializedName("hw_evaluated")
        val hwEvaluated: Int?
    )
}
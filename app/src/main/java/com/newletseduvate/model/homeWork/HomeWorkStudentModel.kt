package com.newletseduvate.model.homeWork

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class HomeWorkStudentModel(
    @SerializedName("id")
    val id: Int,
    @SerializedName("subject")
    val subject: Int,
    @SerializedName("hw_status")
    val hwStatus: HwStatus,
    var classDate: String?
):Serializable {
    data class HwStatus(
        @SerializedName("is_opened")
        val isOpened: Boolean,
        @SerializedName("is_submitted")
        val isSubmitted: Boolean,
        @SerializedName("is_evaluated")
        val isEvaluated: Boolean
    ): Serializable
}
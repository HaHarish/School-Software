package com.newletseduvate.model.online_class

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class TeacherViewClassModel(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("presenter_url")
    val presenterUrl: String?,
    @SerializedName("join_url")
    val joinUrl: String?,
    @SerializedName("online_class")
    val onlineClass: OnlineClass?
): Serializable {
    data class OnlineClass(
        @SerializedName("id")
        val id: Int?,
        @SerializedName("batch_id")
        val batchId: Any?,
        @SerializedName("seat_left")
        val seatLeft: Any?,
        @SerializedName("teacher")
        val teacher: Teacher?,
        @SerializedName("aol_batch_id")
        val aolBatchId: Any?,
        @SerializedName("cource_id")
        val courceId: Int?,
        @SerializedName("title")
        val title: String?,
        @SerializedName("start_time")
        val startTime: String?,
        @SerializedName("end_time")
        val endTime: String?,
        @SerializedName("is_canceled")
        val isCanceled: Boolean?
    ): Serializable {
        data class Teacher(
            @SerializedName("tutor_id")
            val tutorId: Any?,
            @SerializedName("email")
            val email: Any?,
            @SerializedName("roles")
            val roles: Any?
        ): Serializable
    }
}
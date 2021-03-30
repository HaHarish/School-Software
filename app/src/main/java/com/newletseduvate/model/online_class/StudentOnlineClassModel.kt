package com.newletseduvate.model.online_class


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class StudentOnlineClassModel(
    @SerializedName("id")
    val id: Int,
    @SerializedName("presenter_url")
    val presenterUrl: Any,
    @SerializedName("join_url")
    val joinUrl: String,
    @SerializedName("online_class")
    val onlineClass: OnlineClass
) : Serializable {
    data class OnlineClass(
        @SerializedName("id")
        val id: Int,
        @SerializedName("batch_id")
        val batchId: Any,
        @SerializedName("aol_batch_id")
        val aolBatchId: Any,
        @SerializedName("course_id")
        val courseId: Int,
        @SerializedName("title")
        val title: String,
        @SerializedName("start_time")
        val startTime: String,
        @SerializedName("end_time")
        val endTime: String,
        @SerializedName("course_name")
        val courseName: String?
    ) : Serializable
}
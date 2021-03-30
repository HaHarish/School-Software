package com.newletseduvate.model.online_class


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CreateClassRequest(
    @SerializedName("user_id")
    var userId: Int?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("duration")
    val duration: String?,
    @SerializedName("subject_id")
    var subjectId: String?,
    @SerializedName("tutor_id")
    val tutorId: Int?,
    @SerializedName("tutor_emails")
    val tutorEmails: String?,
    @SerializedName("role")
    val role: String?,
    @SerializedName("start_time")
    val startTime: String?,
    @SerializedName("no_of_week")
    var noOfWeek: Int?,
    @SerializedName("is_recurring")
    var isRecurring: Int?,
    @SerializedName("class_type")
    val classType: Int?,
    @SerializedName("section_mapping_ids")
    val sectionMappingIds: String?,
    @SerializedName("week_days")
    val weekDays: ArrayList<String>?,
    @SerializedName("student_ids")
    var studentIds: ArrayList<Int>?,
    @SerializedName("join_limit")
    val joinLimit: String?,
    @SerializedName("course")
    var course: Int?,
    @SerializedName("final_price")
    var final_price: Int?,
    @SerializedName("price")
    var price: Int?,
) : Serializable
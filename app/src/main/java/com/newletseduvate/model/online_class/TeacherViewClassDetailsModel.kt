package com.newletseduvate.model.online_class

import com.google.gson.annotations.SerializedName

data class TeacherViewClassDetailsModel(
    @SerializedName("date")
    val date: String?,
    @SerializedName("end_time")
    val endTime: String?,
    @SerializedName("feed_back")
    val feedBack: String?,
    @SerializedName("start_time")
    val startTime: String?,
    @SerializedName("is_accepted")
    val isAccepted: Boolean?,
    @SerializedName("is_attended")
    val isAttended: Boolean?,
    @SerializedName("is_restricted")
    val isRestricted: Boolean?,
    @SerializedName("is_feedback_given")
    val isFeedbackGiven: Boolean?,
    var currentServerTime: String,
    var zoomUrl : String
)
package com.newletseduvate.model.online_class

import androidx.databinding.ObservableBoolean
import com.google.gson.annotations.SerializedName

data class OnlineClassStudentOcDetailsModel(
    @SerializedName("date")
    val date: String,
    @SerializedName("end_time")
    val endTime: String,
    @SerializedName("feed_back")
    val feedBack: String,
    @SerializedName("start_time")
    val startTime: String,
    @SerializedName("is_accepted")
    val isAccepted: Boolean,
    @SerializedName("is_attended")
    val isAttended: Boolean,
    @SerializedName("is_restricted")
    val isRestricted: Boolean,
    @SerializedName("is_feedback_given")
    val isFeedbackGiven: Boolean,
){

    fun convertToModified(currentServerTime: String): OnlineClassStudentOcDetailsModifiedModel{
        return OnlineClassStudentOcDetailsModifiedModel(
            this.date,
            this.endTime,
            this.feedBack,
            this.startTime,
            ObservableBoolean(this.isAccepted),
            ObservableBoolean(this.isAttended),
            ObservableBoolean(this.isRestricted),
            ObservableBoolean(this.isFeedbackGiven),
            currentServerTime
        )
    }
}
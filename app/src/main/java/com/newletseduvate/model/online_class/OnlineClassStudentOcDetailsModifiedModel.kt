package com.newletseduvate.model.online_class

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

data class OnlineClassStudentOcDetailsModifiedModel(
    val date: String,
    val endTime: String,
    val feedBack: String,
    val startTime: String,
    val isAccepted: ObservableBoolean,
    val isAttended: ObservableBoolean,
    val isRestricted: ObservableBoolean,
    val isFeedbackGiven: ObservableBoolean,
    val currentServerTime: String
) : Serializable {
    val isAcceptVisible = object : ObservableField<Boolean>(
        isAccepted,
        isRestricted
    ) {
        override fun get(): Boolean {
            return !(isAccepted.get()
                    || isRestricted.get())
        }
    }

    val isJoinEnabled = object : ObservableField<Boolean>(isAccepted) {
        override fun get(): Boolean {

            try {

                if (currentServerTime.isNotEmpty() && date.isNotEmpty()) {

                    val timeFormat =SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault())

                    val cal = Calendar.getInstance()

                    val currServerTime = timeFormat.parse(currentServerTime)
                    val classStartTime = timeFormat.parse("$date $startTime")
                    val classEndTime = timeFormat.parse("$date $endTime")

                    cal.time = classStartTime!!
                    cal.add(Calendar.MINUTE, -5)
                    val joinEnableTime = cal.time

                    return currServerTime.after(joinEnableTime) && currServerTime.before(classEndTime)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return false
        }
    }

}
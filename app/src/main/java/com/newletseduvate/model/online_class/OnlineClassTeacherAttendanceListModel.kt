package com.newletseduvate.model.online_class

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableInt
import com.google.gson.annotations.SerializedName
import com.newletseduvate.viewmodels.OnlineClassTeacherAttendanceViewModel

data class OnlineClassTeacherAttendanceListModel(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("user")
    val user: UserModel?,
    @SerializedName("zoom_meeting")
    val zoomMeeting: ZoomMeeting?,
    @SerializedName("join_time")
    val joinTime: String?,
    @SerializedName("batch")
    val batch: Any?,
    @SerializedName("attendance_details")
    val attendanceDetails: AttendanceDetails?,
    var viewModel: OnlineClassTeacherAttendanceViewModel,
    var attendedCount: ObservableInt,
    var absentCount: ObservableInt,
    var isAttended:ObservableBoolean
) {
    data class UserModel(
        @SerializedName("id")
        val id: Int?,
        @SerializedName("user")
        val user: User?,
        @SerializedName("erp_id")
        val erpId: String?
    ) {
        data class User(
            @SerializedName("id")
            val id: Int?,
            @SerializedName("username")
            val username: String?,
            @SerializedName("email")
            val email: String?,
            @SerializedName("first_name")
            val firstName: String?
        )
    }

    data class ZoomMeeting(
        @SerializedName("id")
        val id: Int?,
        @SerializedName("tutor")
        val tutor: List<Tutor?>?,
        @SerializedName("join_time")
        val joinTime: String?,
        @SerializedName("online_class")
        val onlineClass: OnlineClass?,
        @SerializedName("uuid")
        val uuid: String?,
        @SerializedName("meeting_id")
        val meetingId: String?,
        @SerializedName("join_url")
        val joinUrl: String?,
        @SerializedName("teacher_rating")
        val teacherRating: Any?
    ) {
        data class Tutor(
            @SerializedName("id")
            val id: Int?,
            @SerializedName("user")
            val user: User?,
            @SerializedName("erp_id")
            val erpId: String?
        ) {
            data class User(
                @SerializedName("id")
                val id: Int?,
                @SerializedName("username")
                val username: String?,
                @SerializedName("email")
                val email: String?,
                @SerializedName("first_name")
                val firstName: String?
            )
        }

        data class OnlineClass(
            @SerializedName("id")
            val id: Int?,
            @SerializedName("title")
            val title: String?,
            @SerializedName("subject")
            val subject: List<Subject?>?,
            @SerializedName("start_time")
            val startTime: String?,
            @SerializedName("end_time")
            val endTime: String?,
            @SerializedName("section_mapping")
            val sectionMapping: List<Int?>?,
            @SerializedName("grade")
            val grade: List<Any?>?,
            @SerializedName("aol_batch")
            val aolBatch: Any?,
            @SerializedName("batch")
            val batch: Any?
        ) {
            data class Subject(
                @SerializedName("id")
                val id: Int?,
                @SerializedName("subject_name")
                val subjectName: String?
            )
        }
    }

    data class AttendanceDetails(
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
        val isFeedbackGiven: Boolean?
    )
}
package com.newletseduvate.model.online_class


import com.google.gson.annotations.SerializedName

data class OnlineClassTeacherAttendanceListResponse(
    @SerializedName("current_page")
    val currentPage: String?,
    @SerializedName("page_size")
    val pageSize: String?,
    @SerializedName("count")
    val count: Int?,
    @SerializedName("total_pages")
    val totalPages: Int?,
    @SerializedName("attended_count")
    val attendedCount: Int?,
    @SerializedName("notattended_count")
    val notattendedCount: Int?,
    @SerializedName("data")
    val `data`: ArrayList<OnlineClassTeacherAttendanceListModel>?
)
package com.newletseduvate.model.online_class


import com.google.gson.annotations.SerializedName

data class StudentOnlineClassResponse(
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("current_page")
    val currentPage: String,
    @SerializedName("page_size")
    val pageSize: String,
    @SerializedName("count")
    val count: Int,
    @SerializedName("current_server_time")
    val currentServerTime: String,
    @SerializedName("data")
    val `data`: List<StudentOnlineClassModel>
)
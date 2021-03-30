package com.newletseduvate.model.online_class

import com.google.gson.annotations.SerializedName

data class OnlineClassAttendResourceModel(
    @SerializedName("files")
    var files: ArrayList<String?>?,
    @SerializedName("is_delete")
    var isDelete: Boolean?,
    @SerializedName("class_date")
    var classDate: String?,
    @SerializedName("description")
    var description: String?,
    @SerializedName("uploaded_at")
    var uploadedAt: String?,
    @SerializedName("uploaded_by")
    var uploadedBy: Int?
)
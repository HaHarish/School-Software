package com.newletseduvate.model.online_class


import com.google.gson.annotations.SerializedName

data class OnlineClassTeacherResourceFilesResponse(
    @SerializedName("status_code")
    val statusCode: Int?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("result")
    val result: ArrayList<Result>?
) {
    data class Result(
        @SerializedName("files")
        val files: ArrayList<String>?,
        @SerializedName("is_delete")
        val isDelete: Boolean?,
        @SerializedName("class_date")
        val classDate: String?,
        @SerializedName("updated_at")
        val updatedAt: String?,
        @SerializedName("updated_by")
        val updatedBy: Int?,
        @SerializedName("description")
        val description: Any?,
        @SerializedName("uploaded_at")
        val uploadedAt: String?,
        @SerializedName("uploaded_by")
        val uploadedBy: Int?
    )
}
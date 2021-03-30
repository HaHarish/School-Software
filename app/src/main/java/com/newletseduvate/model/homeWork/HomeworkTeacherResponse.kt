package com.newletseduvate.model.homeWork


import com.google.gson.annotations.SerializedName

data class HomeworkTeacherResponse(
    @SerializedName("status_code")
    val statusCode: Int?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("data")
    val `data`: Data?
) {
    data class Data(
        @SerializedName("header")
        val header: ArrayList<Header>?,
        @SerializedName("rows")
        val rows: ArrayList<Row>?
    ) {
        data class Header(
            @SerializedName("id")
            val id: Int?,
            @SerializedName("subject_name")
            val subjectName: String?,
            @SerializedName("subject_slag")
            val subjectSlag: String?
        )

        data class Row(
            @SerializedName("can_upload")
            val canUpload: Boolean,
            @SerializedName("class_date")
            val classDate: String,
            @SerializedName("hw_details")
            val hwDetails: ArrayList<HomeworkTeacherModel>?
        )
    }
}
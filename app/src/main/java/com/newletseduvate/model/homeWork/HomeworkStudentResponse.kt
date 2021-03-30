package com.newletseduvate.model.homeWork

import com.google.gson.annotations.SerializedName

data class HomeworkStudentResponse(
    @SerializedName("status_code")
    val statusCode: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val `data`: Data?
) {
    data class Data(
        @SerializedName("header")
        val header: Header?,
        @SerializedName("rows")
        val rows: List<Row>?
    ) {
        data class Header(
            @SerializedName("id")
            val id: Int,
            @SerializedName("mandatory_subjects")
            val mandatorySubjects: List<MandatorySubject>?,
            @SerializedName("optional_subjects")
            val optionalSubjects: List<OptionalSubject>?,
            @SerializedName("others_subjects")
            val othersSubjects: List<OthersSubject>?,
            @SerializedName("is_hw_ration")
            val isHwRation: Boolean,
            @SerializedName("is_top_performers")
            val isTopPerformers: Boolean
        ) {
            data class MandatorySubject(
                @SerializedName("id")
                val id: Int?,
                @SerializedName("subject_slag")
                val subjectSlag: String?
            )

            data class OptionalSubject(
                @SerializedName("id")
                val id: Int?,
                @SerializedName("subject_slag")
                val subjectSlag: String?
            )

            data class OthersSubject(
                @SerializedName("id")
                val id: Int?,
                @SerializedName("subject_slag")
                val subjectSlag: String?
            )
        }

        data class Row(
            @SerializedName("class_date")
            val classDate: String,
            @SerializedName("hw_details")
            val hwDetails: List<HomeWorkStudentModel>
        )
    }
}
package com.newletseduvate.model.online_class


import com.google.gson.annotations.SerializedName

data class OnlineClassTeacherSubSectionResponse(
    @SerializedName("status_code")
    val statusCode: Int?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("data")
    val `data`: Data?
) {
    data class Data(
        @SerializedName("section")
        val section: ArrayList<SectionMappingModel>?,
        @SerializedName("subject")
        val subject: ArrayList<SubjectModel>?
    ) {

    }
}

//data class Section(
//    @SerializedName("id")
//    val id: Int?,
//    @SerializedName("section_id")
//    val sectionId: Int?,
//    @SerializedName("section__section_name")
//    val sectionSectionName: String?
//)
//
//data class Subject(
//    @SerializedName("subject__id")
//    val subjectId: Int?,
//    @SerializedName("section__id")
//    val sectionId: Int?,
//    @SerializedName("subject__subject_name")
//    val subjectSubjectName: String?
//)
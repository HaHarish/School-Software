package com.newletseduvate.model.diary.teacher


import com.google.gson.annotations.SerializedName

data class TeacherDiaryCreateRequest(
    @SerializedName("title")
    var title: String?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("documents")
    var documents: List<String?>?,
    @SerializedName("branch")
    var branch: Int?,
    @SerializedName("grade")
    var grade: List<Int?>?,
    @SerializedName("mapping_bgs")
    var mappingBgs: List<Int?>?,
    @SerializedName("user_id")
    var userId: List<Int?>?,
    @SerializedName("dairy_type")
    var dairyType: Int?
)
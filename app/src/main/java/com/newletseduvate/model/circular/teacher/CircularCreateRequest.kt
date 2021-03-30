package com.newletseduvate.model.circular.teacher


import com.google.gson.annotations.SerializedName

data class CircularCreateRequest(
    @SerializedName("circular_name")
    var circularName: String?,
    @SerializedName("description")
    var description: String?,
    @SerializedName("module_name")
    var moduleName: String?,
    @SerializedName("media")
    var media: ArrayList<String>?,
    @SerializedName("Branch")
    var branch: List<Int>?,
    @SerializedName("grades")
    var grades: List<Int>?,
    @SerializedName("sections")
    var sections: List<Int>?,
    @SerializedName("academic_year")
    var academicYear: Int?
)
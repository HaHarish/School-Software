package com.newletseduvate.model.circular.teacher


import com.google.gson.annotations.SerializedName

data class CircularTeacherDeleteRequest(
    @SerializedName("circular_id")
    var circularId: Int?,
    @SerializedName("id_delete")
    var idDelete: Boolean?
)
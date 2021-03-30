package com.newletseduvate.model.online_class

import com.google.gson.annotations.SerializedName
import com.newletseduvate.model.BottomSheetHelper
import com.newletseduvate.model.BottomSheetItem

data class CourseModel(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("course_name")
    val courseName: String?,
    var selected : Boolean = false
) : BottomSheetHelper {
    override fun convertToBottomSheetItem(): BottomSheetItem {
        return BottomSheetItem(id, courseName, selected)
    }
}
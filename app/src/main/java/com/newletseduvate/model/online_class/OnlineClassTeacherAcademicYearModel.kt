package com.newletseduvate.model.online_class

import com.google.gson.annotations.SerializedName
import com.newletseduvate.model.BottomSheetHelper
import com.newletseduvate.model.BottomSheetItem

data class OnlineClassTeacherAcademicYearModel(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("session_year")
    val sessionYear: String?
) : BottomSheetHelper{
    override fun convertToBottomSheetItem(): BottomSheetItem {
        return BottomSheetItem(id, sessionYear)
    }

}
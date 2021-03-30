package com.newletseduvate.model.online_class

import com.google.gson.annotations.SerializedName
import com.newletseduvate.model.BottomSheetHelper
import com.newletseduvate.model.BottomSheetItem

data class GradeMappingModel(
    @SerializedName("grade_id")
    val gradeId: Int?,
    @SerializedName("grade__grade_name")
    val gradeGradeName: String?,
    var selected : Boolean = false
) : BottomSheetHelper{
    override fun convertToBottomSheetItem(): BottomSheetItem {
        return BottomSheetItem(gradeId, gradeGradeName, selected)
    }
}
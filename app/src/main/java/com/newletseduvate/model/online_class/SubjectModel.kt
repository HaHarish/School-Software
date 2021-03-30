package com.newletseduvate.model.online_class

import com.google.gson.annotations.SerializedName
import com.newletseduvate.model.BottomSheetHelper
import com.newletseduvate.model.BottomSheetItem

data class SubjectModel(
    @SerializedName("subject__id")
    val subjectId: Int?,
    @SerializedName("subject__subject_name")
    val subjectSubjectName: String?,
    var selected : Boolean = false
) : BottomSheetHelper {
    override fun convertToBottomSheetItem(): BottomSheetItem {
        return BottomSheetItem(subjectId, subjectSubjectName, selected)
    }
}
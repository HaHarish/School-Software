package com.newletseduvate.model.online_class

import com.google.gson.annotations.SerializedName
import com.newletseduvate.model.BottomSheetHelper
import com.newletseduvate.model.BottomSheetItem

data class CreateClassTutorsModel(
    @SerializedName("tutor_id")
    val tutorId: Int?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("roles")
    val roles: Int?,
    var selected: Boolean = false
) : BottomSheetHelper{
    override fun convertToBottomSheetItem(): BottomSheetItem {
        return BottomSheetItem(tutorId, email, selected)
    }

}
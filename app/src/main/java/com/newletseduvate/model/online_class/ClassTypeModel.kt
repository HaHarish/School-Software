package com.newletseduvate.model.online_class


import com.google.gson.annotations.SerializedName
import com.newletseduvate.model.BottomSheetHelper
import com.newletseduvate.model.BottomSheetItem

data class ClassTypeModel(
    @SerializedName("id")
    val id: Int,
    @SerializedName("type")
    val type: String
) : BottomSheetHelper{
    override fun convertToBottomSheetItem(): BottomSheetItem {
        return BottomSheetItem(id, type)
    }
}
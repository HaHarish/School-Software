package com.newletseduvate.model.online_class

import com.google.gson.annotations.SerializedName
import com.newletseduvate.model.BottomSheetHelper
import com.newletseduvate.model.BottomSheetItem

data class BranchModel(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("branch_name")
    val branchName: String?,
    var selected : Boolean = false
) : BottomSheetHelper {
    override fun convertToBottomSheetItem(): BottomSheetItem {
        return BottomSheetItem(id, branchName, selected)
    }
}
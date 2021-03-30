package com.newletseduvate.model.online_class

import com.google.gson.annotations.SerializedName
import com.newletseduvate.model.BottomSheetHelper
import com.newletseduvate.model.BottomSheetItem

data class SectionMappingModel(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("section_id")
    val sectionId: Int?,
    @SerializedName("section__section_name")
    val sectionSectionName: String?,
    var selected : Boolean = false
) : BottomSheetHelper{
    override fun convertToBottomSheetItem(): BottomSheetItem {
        return BottomSheetItem(sectionId, sectionSectionName, selected)
    }

}
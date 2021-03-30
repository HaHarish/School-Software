package com.newletseduvate.model.online_class


import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.newletseduvate.model.BottomSheetHelper
import com.newletseduvate.model.BottomSheetItem

data class GradeModel(
    @SerializedName("grade_id")
    val gradeId: Int,
    @SerializedName("grade_name")
    val gradeName: String?,
    var selected: Boolean = false
) : Parcelable, BottomSheetHelper {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readByte() != 0.toByte())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(gradeId)
        parcel.writeString(gradeName)
        parcel.writeByte(if (selected) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GradeModel> {
        override fun createFromParcel(parcel: Parcel): GradeModel {
            return GradeModel(parcel)
        }

        override fun newArray(size: Int): Array<GradeModel?> {
            return arrayOfNulls(size)
        }
    }

    override fun convertToBottomSheetItem(): BottomSheetItem {
        return BottomSheetItem(gradeId, gradeName, selected)
    }
}
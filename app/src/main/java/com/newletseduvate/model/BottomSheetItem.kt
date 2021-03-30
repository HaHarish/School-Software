package com.newletseduvate.model

import android.os.Parcel
import android.os.Parcelable

data class BottomSheetItem(
    val id: Int?,
    val name: String?,
    var selected: Boolean = false
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readByte() != 0.toByte()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        if (id != null) {
            parcel.writeInt(id)
        }
        parcel.writeString(name)
        parcel.writeByte(if (selected) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BottomSheetItem> {
        override fun createFromParcel(parcel: Parcel): BottomSheetItem {
            return BottomSheetItem(parcel)
        }

        override fun newArray(size: Int): Array<BottomSheetItem?> {
            return arrayOfNulls(size)
        }
    }
}
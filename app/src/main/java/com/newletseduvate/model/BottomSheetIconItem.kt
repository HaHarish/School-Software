package com.newletseduvate.model

import android.os.Parcel
import android.os.Parcelable

data class BottomSheetIconItem(
    val id: Int,
    val iconId:Int,
    val name: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeInt(iconId)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BottomSheetIconItem> {
        override fun createFromParcel(parcel: Parcel): BottomSheetIconItem {
            return BottomSheetIconItem(parcel)
        }

        override fun newArray(size: Int): Array<BottomSheetIconItem?> {
            return arrayOfNulls(size)
        }
    }
}
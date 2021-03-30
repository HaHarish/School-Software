package com.newletseduvate.utils.extensions

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import com.newletseduvate.model.BottomSheetHelper
import com.newletseduvate.model.BottomSheetItem
import com.newletseduvate.model.online_class.BranchModel
import com.newletseduvate.ui.bottom_sheets.MultiSelectionBottomSheetFragment
import com.newletseduvate.ui.bottom_sheets.SingleSelectionBottomSheetFragment

fun ArrayList<BottomSheetItem>.showSingleSelectionBottomSheetWithArrayList(
    fragmentManager: FragmentManager,
    bottomSheetId: Int,
    selectedItemId: Int
){

    for (ele in this) {
        if (ele.id == selectedItemId) {
            ele.selected = true
            break
        }
    }
    val fragment = SingleSelectionBottomSheetFragment(bottomSheetId, this)
    fragment.show(fragmentManager, fragment.tag)

}

fun ArrayList<BottomSheetItem>.showMultiSelectionBottomSheetWithArrayList(
    fragmentManager: FragmentManager,
    bottomSheetId: Int
){
    val fragment = MultiSelectionBottomSheetFragment(bottomSheetId, this)
    fragment.show(fragmentManager, fragment.tag)
}

fun List<BottomSheetHelper>.getBottomSheetList(): ArrayList<BottomSheetItem> {
    val arrayList = ArrayList<BottomSheetItem>()
    forEach {
        arrayList.add(it.convertToBottomSheetItem())
    }
    return arrayList
}

fun List<BottomSheetHelper>?.getBottomSheetItem(id: Int): BottomSheetItem {

    this?.forEach {
        if (it.convertToBottomSheetItem().id == id)
            return it.convertToBottomSheetItem()
    }
    return BottomSheetItem(-1, "", false)
}

fun List<BottomSheetHelper>?.getSelectedItemsInString(): String {

    this?.forEach {
        val bottomSheetItem = it.convertToBottomSheetItem()
        if(bottomSheetItem.selected)
            return bottomSheetItem.id.toString()
    }

    return ""
}


fun ArrayList<BottomSheetItem>?.getSelectedItemsInString(): String{

    var selected = ""

    this?.forEach {
        if(it.selected && selected.isEmpty())
            selected = it.name.toString()
        else if(it.selected)
            selected += ", ${it.name}"
    }

    return selected
}

fun List<BottomSheetHelper>?.getSelectedBottomSheetItemFromList(): BottomSheetItem {

    this?.forEach {
        val bottomSheetItem = it.convertToBottomSheetItem()
        if(bottomSheetItem.selected)
            return bottomSheetItem
    }

    return BottomSheetItem(-1, "")
}
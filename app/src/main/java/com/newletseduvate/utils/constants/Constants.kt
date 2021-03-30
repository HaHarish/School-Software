package com.newletseduvate.utils.constants

import android.view.ViewGroup
import android.widget.LinearLayout
import com.google.android.material.tabs.TabLayout

/**
 * Created by SHASHANK BHAT on 17-Feb-21.
 *
 *
 */
object Constants {

    val HTTP = "http://"
    val HTTPS = "https://"

    const val FILTER_DEBOUNCE_TIME = 200L

    const val BOTTOM_SHEET_ITEM_TEXT_SIZE_SELECTED = 16f
    const val BOTTOM_SHEET_ITEM_TEXT_SIZE_UNSELECTED = 14f

    const val MODULE_ID = "Module_id"

    fun allotEachTabWithEqualWidth(tabLayout: TabLayout) {
        val slidingTabStrip = tabLayout.getChildAt(0) as ViewGroup
        for (i in 0 until tabLayout.tabCount) {
            val tab = slidingTabStrip.getChildAt(i)
            val layoutParams = tab.layoutParams as LinearLayout.LayoutParams
            layoutParams.weight = 1f
            tab.layoutParams = layoutParams
        }
    }

    object DateFormat{

        const val YYYYMMDD = "yyyy-MM-dd"
        const val YYYYMMDDTHHMMSS = "yyyy-MM-dd'T'hh:mm:ss"
        const val DDMMYYYY = "dd MMM yyyy"
        const val HMMA = "h:mm a"
        const val DDMMYYYY_ = "dd-MM-yyyy"
    }
}
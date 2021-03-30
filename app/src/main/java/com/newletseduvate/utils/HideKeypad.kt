package com.newletseduvate.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

object HideKeypad {

    fun hide(context: Context, v: View){
        val iMm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        iMm.hideSoftInputFromWindow(v.windowToken, 0)
        v.clearFocus()
    }
}
@file:JvmName("Extensions")

package com.newletseduvate.utils.extensions

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import com.google.android.material.textfield.TextInputLayout

fun View.setSafeOnClickListener(onSafeClick: (View) -> Unit) {
    val safeClickListener = SafeClickListener {
        onSafeClick(it)
    }
    setOnClickListener(safeClickListener)
}

fun TextInputLayout.addTextWatchers(editText: EditText) {
    val textInputLayout: TextInputLayout = this
    editText.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(str: Editable?) {
            if (str?.length!! >= 1) {
                textInputLayout.error = null
            }
        }
    })
}
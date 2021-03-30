package com.newletseduvate.utils.oes_tool

import android.content.Context
import androidx.lifecycle.ViewModel
import com.newletseduvate.R

class Custom_BottomToolViewModel(private var model: ToolModel, private var context: Context) :
        ViewModel() {
    fun name(): String {
        return model.toolName
    }

    fun getImage(): Int {
        return model.toolIcon
    }

    fun getTextColor(): Int {
        return if (model.selected) context.resources.getColor(R.color.colorPrimary) else context.resources.getColor(
                R.color.black
        )
    }
}
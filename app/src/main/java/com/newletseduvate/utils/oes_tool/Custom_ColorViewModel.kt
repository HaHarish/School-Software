package com.newletseduvate.utils.oes_tool

import androidx.lifecycle.ViewModel

class Custom_ColorViewModel(var model: Int) : ViewModel() {
    fun background(): Int {
        return model
    }
}
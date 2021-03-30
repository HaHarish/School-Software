package com.newletseduvate.utils.oes_tool

import android.app.Application
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.newletseduvate.R

class AddTextViewModel(private var context: Application) : AndroidViewModel(context) {
    private var colorPickerColors: ArrayList<Int> = ArrayList()
    var colordata: MutableLiveData<ArrayList<Int>> = MutableLiveData<ArrayList<Int>>()

    init {
        colorPickerColors.add(ContextCompat.getColor(context, R.color.blue_color_picker))
        colorPickerColors.add(ContextCompat.getColor(context, R.color.brown_color_picker))
        colorPickerColors.add(ContextCompat.getColor(context, R.color.green_color_picker))
        colorPickerColors.add(ContextCompat.getColor(context, R.color.orange_color_picker))
        colorPickerColors.add(ContextCompat.getColor(context, R.color.red_color_picker))
        colorPickerColors.add(ContextCompat.getColor(context, R.color.black))
        colorPickerColors.add(ContextCompat.getColor(context, R.color.red_orange_color_picker))
        colorPickerColors.add(ContextCompat.getColor(context, R.color.sky_blue_color_picker))
        colorPickerColors.add(ContextCompat.getColor(context, R.color.violet_color_picker))
        colorPickerColors.add(ContextCompat.getColor(context, R.color.white))
        colorPickerColors.add(ContextCompat.getColor(context, R.color.yellow_color_picker))
        colorPickerColors.add(ContextCompat.getColor(context, R.color.yellow_green_color_picker))
        colordata.apply {
            value = colorPickerColors
        }


    }

}
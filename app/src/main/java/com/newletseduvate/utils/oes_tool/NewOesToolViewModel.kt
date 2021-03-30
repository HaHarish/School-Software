package com.newletseduvate.utils.oes_tool

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import java.io.File
import javax.inject.Inject

class NewOesToolViewModel @Inject constructor() : ViewModel() {

    var currPage = MutableLiveData(0)
    var prevPage = MutableLiveData(0)

    var disposable = CompositeDisposable()

    var bitmapList: ArrayList<Bitmap> = ArrayList()
    var bitmapStaticList: ArrayList<Bitmap> = ArrayList()
    var imageFiles = ArrayList<File>()

    var isSaved = MutableLiveData(false)
    var isBrushEnabled = MutableLiveData(false)

    lateinit var fileSavedIndex : BooleanArray

    val brushColor = MutableLiveData(-1)
    val opacity = MutableLiveData(-1)
    val brushSize = MutableLiveData(-1)


    fun clear(){
        currPage.value = 0
        prevPage.value = 0

        bitmapList.clear()
        bitmapStaticList.clear()
        imageFiles.clear()

        isSaved.value = false
        isBrushEnabled.value = false

        brushColor.value = -1
        opacity.value = -1
        brushSize.value = -1
    }
}

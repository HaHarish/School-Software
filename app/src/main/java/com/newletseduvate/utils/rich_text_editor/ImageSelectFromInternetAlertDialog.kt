package com.newletseduvate.utils.rich_text_editor

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.chinalwb.are.Util
import com.chinalwb.are.spans.AreImageSpan
import com.chinalwb.are.styles.IARE_Image
import com.newletseduvate.R

class ImageSelectFromInternetAlertDialog(
        private var context: Context,
        private var areImage: IARE_Image,
        private var requestCode: Int
) {
    var editText: EditText? = null
    private var mRootView: View? = null
    private var mDialog: Dialog? = null

    init {
        mRootView = initView()
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Insert image from internet")
        builder.setView(mRootView)
        mDialog = builder.create()
    }

    private fun initView(): View? {
        val layoutInflater = LayoutInflater.from(context)
        val view: View = layoutInflater.inflate(R.layout.are_image_select_from_internet, null)
        editText = view.findViewById(R.id.are_image_select_internet_image_url)
        val insertInternetImage = view.findViewById<TextView>(R.id.are_image_select_insert)
        insertInternetImage.setOnClickListener { insertInternetImage() }
        editText!!.setTextColor(context.resources.getColor(R.color.black))
        editText!!.hint = "Enter image URL"
        return view
    }

    fun show() {
        mDialog!!.show()
    }

    private fun insertInternetImage() {
        val editText = mRootView!!.findViewById<EditText>(R.id.are_image_select_internet_image_url)
        val imageUrl = editText.text.toString()
        if (imageUrl.startsWith("http")
                && (imageUrl.endsWith("png") || imageUrl.endsWith("jpg") || imageUrl.endsWith("jpeg"))
        ) {
            areImage.insertImage(imageUrl, AreImageSpan.ImageType.URL)
            mDialog!!.dismiss()
        } else {
            Util.toast(context, "Not a valid image")
        }
    }
}
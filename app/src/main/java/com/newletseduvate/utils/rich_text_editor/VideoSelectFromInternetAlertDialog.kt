package com.newletseduvate.utils.rich_text_editor

import android.app.Dialog
import android.content.Context
import android.graphics.BitmapFactory
import android.text.Editable
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.chinalwb.are.AREditText
import com.chinalwb.are.Util
import com.newletseduvate.R

class VideoSelectFromInternetAlertDialog(
        private var context: Context,
        private var arEditText: AREditText?
) {
    var editText: EditText? = null
    private var mRootView: View? = null
    private var mDialog: Dialog? = null

    init {
        mRootView = initView()
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Insert video from internet")
        builder.setView(mRootView)
        mDialog = builder.create()
    }

    private fun initView(): View? {
        val layoutInflater = LayoutInflater.from(context)
        val view: View = layoutInflater.inflate(R.layout.are_image_select_from_internet, null)
        editText = view.findViewById(R.id.are_image_select_internet_image_url)
        val insertInternetImage = view.findViewById<TextView>(R.id.are_image_select_insert)
        insertInternetImage.setOnClickListener { insertInternetVideo() }
        editText!!.setTextColor(context.resources.getColor(R.color.black))
        editText!!.hint = "Enter video URL"
        return view
    }

    fun show() {
        mDialog!!.show()
    }

    private fun insertInternetVideo() {
        val editText = mRootView!!.findViewById<EditText>(R.id.are_image_select_internet_image_url)
        val videoUrl = editText.text.toString()
        if ((videoUrl.startsWith("http")
                        && (videoUrl.endsWith("webm") || videoUrl.endsWith("mp4") || videoUrl.endsWith("mkv")) || videoUrl.startsWith("https://youtu.be"))
        ) {
            insertVideo(videoUrl)
            mDialog!!.dismiss()
        } else {
            Util.toast(context, "Not a valid video")
        }
    }

    private fun insertVideo(url: String) {

        if (null != arEditText) {
            val editable: Editable = arEditText!!.editableText
            val start: Int = arEditText!!.selectionStart
            var end: Int = arEditText!!.selectionEnd
            if (start == end) {
                editable.insert(start, url)
                end = start + url.length
            }

            val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.video)
            editable.setSpan(AreVideoSpan(context, bitmap, "", url), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

    }
}
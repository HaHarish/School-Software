package com.newletseduvate.utils.rich_text_editor

import android.content.Context
import android.content.DialogInterface
import android.os.Build
import android.text.Editable
import android.text.Spanned
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import com.chinalwb.are.AREditText
import com.chinalwb.are.spans.AreUrlSpan
import com.chinalwb.are.styles.ARE_Image
import com.chinalwb.are.styles.toolbar.ARE_ToolbarDefault
import com.chinalwb.are.styles.toolitems.*
import com.newletseduvate.R
import com.newletseduvate.utils.constants.Constants

class RichEditor(private var context: Context, private var areToolbar: ARE_ToolbarDefault, private var arEditText: AREditText, hint: String) {

    var areImageView: ImageView? = null
    var are_image: ARE_Image? = null
    private val BLOG_IMAGE_REQ = 1001

    private var layoutInflater: LayoutInflater = LayoutInflater.from(context)

    init {
        initRichEditorToolbar()
        arEditText.hint = hint
    }

    private fun initRichEditorToolbar() {
        val bold: IARE_ToolItem = ARE_ToolItem_Bold()
        val italic: IARE_ToolItem = ARE_ToolItem_Italic()
        val underline: IARE_ToolItem = ARE_ToolItem_Underline()
        val strikethrough: IARE_ToolItem = ARE_ToolItem_Strikethrough()
        val quote: IARE_ToolItem = ARE_ToolItem_Quote()
        val listNumber: IARE_ToolItem = ARE_ToolItem_ListNumber()
        val listBullet: IARE_ToolItem = ARE_ToolItem_ListBullet()
        val hr: IARE_ToolItem = ARE_ToolItem_Hr()
        val link: IARE_ToolItem = ARE_ToolItem_Link()
        val video: IARE_ToolItem = ARE_ToolItem_Video()
        val subscript: IARE_ToolItem = ARE_ToolItem_Subscript()
        val superscript: IARE_ToolItem = ARE_ToolItem_Superscript()
        val left: IARE_ToolItem = ARE_ToolItem_AlignmentLeft()
        val center: IARE_ToolItem = ARE_ToolItem_AlignmentCenter()
        val right: IARE_ToolItem = ARE_ToolItem_AlignmentRight()
        val image: IARE_ToolItem = ARE_ToolItem_Image()
        val at: IARE_ToolItem = ARE_ToolItem_At()
        val backgroundColor: IARE_ToolItem = ARE_ToolItem_BackgroundColor()
        val fontColor: IARE_ToolItem = ARE_ToolItem_FontColor()
        val fontSize: IARE_ToolItem = ARE_ToolItem_FontSize()
        areToolbar.addToolbarItem(bold)
        areToolbar.addToolbarItem(italic)
        areToolbar.addToolbarItem(left)
        areToolbar.addToolbarItem(center)
        areToolbar.addToolbarItem(right)
        areToolbar.addToolbarItem(image)
        areToolbar.addToolbarItem(video)
        areToolbar.addToolbarItem(link)
        areToolbar.addToolbarItem(listNumber)
        areToolbar.addToolbarItem(listBullet)
        areToolbar.addToolbarItem(underline)
        areToolbar.addToolbarItem(strikethrough)
        areToolbar.addToolbarItem(quote)
        areToolbar.addToolbarItem(backgroundColor)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            areToolbar.addToolbarItem(fontColor)
            areToolbar.addToolbarItem(fontSize)
        }
        areToolbar.addToolbarItem(hr)
        areToolbar.addToolbarItem(subscript)
        areToolbar.addToolbarItem(superscript)
        areToolbar.addToolbarItem(at)
        arEditText.setToolbar(areToolbar)
        areImageView = ImageView(context)
        are_image = ARE_Image(areImageView)
        are_image!!.setEditText(arEditText)
        image.getView(context).setOnClickListener {
            val dialog = ImageSelectFromInternetAlertDialog(
                    context,
                    are_image!!, BLOG_IMAGE_REQ
            )
            dialog.show()
        }
        link.getView(context).setOnClickListener { openLinkDialog() }

        video.getView(context).setOnClickListener {
            val dialog = VideoSelectFromInternetAlertDialog(
                    context,
                    arEditText
            )
            dialog.show()
        }

    }

    private fun openLinkDialog() {

        // 1. Instantiate an AlertDialog.Builder with its constructor
        val builder = AlertDialog.Builder(context)

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setTitle(R.string.are_link_title)
        val layoutInflater = layoutInflater
        val areInsertLinkView = layoutInflater.inflate(R.layout.are_link_insert, null)
        val editText = areInsertLinkView.findViewById<View>(R.id.are_insert_link_edit) as EditText
        editText.setTextColor(context.resources.getColor(R.color.black))
        editText.requestFocus()
        builder.setView(areInsertLinkView) // Add the buttons
                .setPositiveButton(R.string.ok, DialogInterface.OnClickListener { dialog, id ->
                    val url = editText.text.toString()
                    if (TextUtils.isEmpty(url)) {
                        dialog.dismiss()
                        return@OnClickListener
                    }
                    insertLink(url)
                })
        builder.setNegativeButton(R.string.cancel) { dialog, id -> dialog.dismiss() }

        // 3. Get the AlertDialog from create()
        val dialog = builder.create()
        dialog.show()
    }

    private fun insertLink(url: String) {
        var url = url
        if (TextUtils.isEmpty(url)) {
            return
        }
        if (!url.startsWith(Constants.HTTP) && !url.startsWith(Constants.HTTPS)) {
            url = Constants.HTTP + url
        }
        if (null != arEditText) {
            val editable: Editable = arEditText.editableText
            val start: Int = arEditText.selectionStart
            var end: Int = arEditText.selectionEnd
            if (start == end) {
                editable.insert(start, url)
                end = start + url.length
            }
            editable.setSpan(AreUrlSpan(url), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

    }

}
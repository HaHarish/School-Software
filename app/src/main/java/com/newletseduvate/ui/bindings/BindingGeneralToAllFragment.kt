package com.newletseduvate.ui.bindings

import android.net.Uri
import android.webkit.WebView
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.newletseduvate.R
import com.newletseduvate.utils.constants.Constants
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

object BindingGeneralToAllFragment {

    @JvmStatic
    @BindingAdapter(value = ["bindDate_YYYYDDMM"], requireAll = true)
    fun bindDate_YYYYDDMM(textView: TextView, time: Long) {
        if (time == 0L) {
            textView.text = ""
        } else {
            val requestDateFormat = SimpleDateFormat(Constants.DateFormat.YYYYMMDD)
            textView.text = requestDateFormat.format(Date(time))
        }

    }

    @JvmStatic
    @BindingAdapter(value = ["bindDate_YYYYDDMM_Start", "bindDate_YYYYDDMM_End"], requireAll = true)
    fun bindDate_StartEndDate(textView: TextView, startTime: Long, endTime: Long) {
        if (startTime == 0L) {
            textView.text = ""
        } else {
            val requestDateFormat = SimpleDateFormat(Constants.DateFormat.YYYYMMDD)
            textView.text = requestDateFormat.format(Date(startTime)) +" - " + requestDateFormat.format(Date(endTime))
        }

    }

    @JvmStatic
    @BindingAdapter(value = ["bindDate_YYYYMMDDTHHMMSS"], requireAll = true)
    fun bindDate_YYYYMMDDTHHMMSS(textView: TextView, time: String) {

        val simpledDateFormat = SimpleDateFormat(Constants.DateFormat.DDMMYYYY)
        val dateFormat = SimpleDateFormat(Constants.DateFormat.YYYYMMDDTHHMMSS)
        textView.text = simpledDateFormat.format(dateFormat.parse(time))
    }

    @JvmStatic
    @BindingAdapter(value = ["bindDate_HMMA"], requireAll = true)
    fun bindDate_HMMA(textView: TextView, time: String) {

        val simpledDateFormat = SimpleDateFormat(Constants.DateFormat.HMMA)
        val dateFormat = SimpleDateFormat(Constants.DateFormat.YYYYMMDDTHHMMSS)
        textView.text = simpledDateFormat.format(dateFormat.parse(time))
    }


    @JvmStatic
    @BindingAdapter(value = ["bindStartDate", "bindEndDate"], requireAll = true)
    fun bindStartAndEndDate(textView: TextView, startTime: Long, endTime: Long) {
        if (startTime == 0L || endTime == 0L) {
            textView.text = ""
        } else {
            val requestDateFormat = SimpleDateFormat(Constants.DateFormat.YYYYMMDD)
            textView.text = requestDateFormat.format(Date(startTime)) + "-" + requestDateFormat.format(Date(endTime))
        }

    }

    @JvmStatic
    @BindingAdapter(value = ["bindImageViewByUrl"], requireAll = true)
    fun bindImageViewByUrl(imageView: ImageView, url: String?) {
        Glide.with(imageView.context)
            .load(url)
            .fitCenter()
            .placeholder(R.drawable.image_shape_empty)
            .into(imageView)

    }

    @JvmStatic
    @BindingAdapter(value = ["bindImageViewByFile"], requireAll = true)
    fun bindImageViewByFile(imageView: ImageView, file: File?) {
        imageView.setImageURI(Uri.fromFile(file))
    }

    @JvmStatic
    @BindingAdapter(value = ["bindHtmlToWebView"], requireAll = true)
    fun bindHtmlToWebView(webView: WebView, answer: String) {
        var answer = answer
        if (answer.contains("<img")) {
            answer = answer.replace(
                "<img",
                "<img style=\"height:auto; max-height:200px; max-width:200px;\" ",
                true
            )
        }
        webView.loadData(answer, "text/html", "UTF-8")
    }

}
package com.newletseduvate.utils.extensions

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.util.Pair
import androidx.databinding.ObservableField
import androidx.databinding.ObservableLong
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.newletseduvate.R
import com.newletseduvate.utils.OnClickAction
import com.newletseduvate.utils.mediaview.OnlineAudioPlayer
import com.newletseduvate.utils.mediaview.VideoAndImageViewer
import java.io.File
import java.util.*


fun View.hideKeyboardOnClick() {
    setOnClickListener { hideKeyboard() }
}

fun View.hideKeyboard() {
    (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).run {
        val currentFocus = (context as AppCompatActivity).currentFocus
        hideSoftInputFromWindow(
            currentFocus?.windowToken,
            InputMethodManager.RESULT_UNCHANGED_SHOWN
        )
        currentFocus?.clearFocus()
    }
}

fun TextInputEditText.setOnDoneClickListener(block: (View) -> Unit) {
    this.setOnEditorActionListener { v, keyCode, _ ->
        (keyCode == EditorInfo.IME_ACTION_DONE).also {
            block(v)
            (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).run {
                this.hideSoftInputFromWindow(
                    windowToken,
                    InputMethodManager.RESULT_UNCHANGED_SHOWN
                )
                clearFocus()
            }
        }
    }

}


fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun View.isLoading(isLoading: Boolean) {
    visibility = if (isLoading) View.VISIBLE else View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.isVisible() = visibility == View.VISIBLE


fun View.snackBar(msg: String, action: String = "") {

    val snackBarView = Snackbar.make(this, msg, Snackbar.LENGTH_SHORT)
        .setActionTextColor(ContextCompat.getColor(context, R.color.white))
        .setTextColor(Color.WHITE)
        .setAction(action) {
            it.hide()
        }
    if (!snackBarView.isShown)
        snackBarView.show()
}

fun View.snackBar(
    msg: String,
    action: String = "",
    isShort: Boolean = false,
    onClickAction: OnClickAction<Unit>
) {

    var duration = Snackbar.LENGTH_LONG
    if (isShort)
        duration = Snackbar.LENGTH_SHORT

    val snackBarView = Snackbar.make(this, msg, duration)
        .setActionTextColor(ContextCompat.getColor(context, R.color.white))
        .setTextColor(Color.WHITE)

    if (action.isNotEmpty())
        snackBarView.setAction(action) {
            // Responds to click on the action
            it.hide()
            onClickAction.invoke(Unit)
        }
    if (!snackBarView.isShown)
        snackBarView.show()
}

fun View.showMaterialDatePicker(
    activity: AppCompatActivity,
    startDate: ObservableLong,
    endDate: ObservableLong,
    function: () -> Unit
) {

    val calendarConstraints = CalendarConstraints.Builder()
    calendarConstraints.setEnd(Calendar.getInstance().timeInMillis)
    val builder = MaterialDatePicker.Builder.dateRangePicker()
    if (startDate.get() > 0L && endDate.get() > 0L) {
        builder.setSelection(Pair(startDate.get(), endDate.get()))
    }
    builder.setCalendarConstraints(calendarConstraints.build())
    val picker = builder.build()

    picker.addOnPositiveButtonClickListener {
        startDate.set(it.first!!)
        endDate.set(it.second!!)
        function()
    }
    picker.show(activity.supportFragmentManager, picker.toString())

}

fun View.datePickerDialog(date: ObservableField<Long>) {
    val resId = R.style.Common_DatePickerDialogTheme

    val calendar = Calendar.getInstance()

    val datePickerDialog = context?.let {
        DatePickerDialog(
            it, resId, { _, year, monthOfYear, dayOfMonth ->
                calendar[year, monthOfYear] = dayOfMonth
                date.set(calendar.time.time)
            }, calendar[Calendar.YEAR], calendar[Calendar.MONTH], calendar[Calendar.DAY_OF_MONTH]
        )
    }
    datePickerDialog?.show()
}


fun View.datePickerDialog(date: ObservableLong) {
    val resId = R.style.Common_DatePickerDialogTheme

    val calendar = Calendar.getInstance()

    val datePickerDialog = context?.let {
        DatePickerDialog(
            it, resId, { _, year, monthOfYear, dayOfMonth ->
                calendar[year, monthOfYear] = dayOfMonth
                date.set(calendar.time.time)
            }, calendar[Calendar.YEAR], calendar[Calendar.MONTH], calendar[Calendar.DAY_OF_MONTH]
        )
    }

    datePickerDialog?.show()
}

fun View.datePickerDialogMinToday(date: ObservableLong) {
    val resId = R.style.Common_DatePickerDialogTheme

    val calendar = Calendar.getInstance()

    val datePickerDialog = context?.let {
        DatePickerDialog(
            it, resId, { _, year, monthOfYear, dayOfMonth ->
                calendar[year, monthOfYear] = dayOfMonth
                date.set(calendar.time.time)
            }, calendar[Calendar.YEAR], calendar[Calendar.MONTH], calendar[Calendar.DAY_OF_MONTH]
        )
    }
    datePickerDialog?.datePicker?.minDate = System.currentTimeMillis()

    datePickerDialog?.show()
}

fun View.timePicker(time : ObservableField<String>){

    val resId = R.style.Common_DatePickerDialogTheme

    val timePickerDialog = context?.let {
        TimePickerDialog(
            it, resId, { _, hour, minute ->
                if(hour>9)
                    time.set("$hour:$minute:00")
                else
                    time.set("0$hour:$minute:00")
            }, 0,0, true
        )
    }
    timePickerDialog?.show()
}

fun Context.openMediaFile(url: String) {

    when {
//            url.endsWith(".pdf") -> {
//                val intent = Intent(context, ViewPdfActivity::class.java)
//                intent.putExtra("url", url)
//                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//                context.startActivity(intent)
//            }

        FileExtension.isImageFile(url) -> {
            val fm: FragmentManager =
                (this as AppCompatActivity).supportFragmentManager
            val expoVideoPlayer = VideoAndImageViewer()
            val bundle = Bundle()
            bundle.putString("url", url)
            bundle.putString("type", VideoAndImageViewer.IMAGE_TAG)
            expoVideoPlayer.arguments = bundle
            expoVideoPlayer.show(fm, "Media Fragment")
        }

        FileExtension.isVideoFile(url) -> {
            val fm: FragmentManager =
                (this as AppCompatActivity).supportFragmentManager
            val expoVideoPlayer = VideoAndImageViewer()
            val bundle = Bundle()
            bundle.putString("url", url)
            bundle.putString("type", VideoAndImageViewer.VIDEO_TAG)
            expoVideoPlayer.arguments = bundle
            expoVideoPlayer.show(fm, "Media Fragment")
        }

        FileExtension.isAudioFile(url) -> {
            val fm: FragmentManager =
                (this as AppCompatActivity).supportFragmentManager
            val onlineAudioPlayer = OnlineAudioPlayer()
            val bundle = Bundle()
            bundle.putString("url", url)
            onlineAudioPlayer.arguments = bundle
            onlineAudioPlayer.show(fm, "Media Fragment")
        }

        else -> openInChrome(url)
    }

}

fun Context.openInChrome(url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    intent.setPackage("com.android.chrome")
    try {
        startActivity(intent)
    } catch (ex: ActivityNotFoundException) {
        ex.printStackTrace()
        // Chrome browser presumably not installed so allow user to choose instead
        // intent.setPackage(null)
        // this.startActivity(intent)
    }
}

fun Context.quality(path: String): Int {
    val size = (File(path).length() / (1024 * 1024.toDouble()))//MB
    return when {
        size < 1 -> 80
        size >= 1 && size < 2 -> 50
        size >= 2 && size < 3 -> 35
        size >= 3 && size < 4 -> 20
        else -> 10
    }

}
package com.newletseduvate.utils.extensions

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.WindowManager
import com.newletseduvate.R
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CustomProgressBar {

    companion object {
        private val progressBar = CustomProgressBar()
        private lateinit var dialog: Dialog

        fun getInstance(context: Context): CustomProgressBar {
            if (!::dialog.isInitialized) {
                synchronized(CustomProgressBar::class.java) {
                    if (!::dialog.isInitialized) {
                        dialog = Dialog(context)
                    }
                }
            }
            return progressBar
        }
    }

    fun showProgressBar() {
        try {
            dialog.setContentView(R.layout.layout_progress_bar)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window!!.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT
            )
            dialog.show()
        } catch (ex: Exception) { }
    }

    fun dismissProgressBar() {
        try {
            dialog.dismiss()
        } catch (ex: Exception) { }
    }
}
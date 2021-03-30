package com.newletseduvate.utils.extensions

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.WindowManager
import com.newletseduvate.R
import javax.inject.Singleton

@Singleton
class TermsAndConditions {

    companion object {
        private val progressBar = TermsAndConditions()
        private lateinit var dialog: Dialog

        fun getInstance(context: Context): TermsAndConditions {
            if (!::dialog.isInitialized) {
                synchronized(TermsAndConditions::class.java) {
                    if (!::dialog.isInitialized) {
                        dialog = Dialog(context)
                    }
                }
            }
            return progressBar
        }
    }

    fun showTermsAndConditions() {
        try {
            dialog.setContentView(R.layout.layout_terms_and_conditions)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window!!.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
            dialog.show()
        } catch (ex: Exception) { }
    }

    fun dismissTermsAndConditions() {
        try {
            dialog.dismiss()
        } catch (ex: Exception) { }
    }
}
package com.newletseduvate.base

import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.newletseduvate.app
import com.newletseduvate.di.AppComponent

abstract class BaseActivity : AppCompatActivity() {

    val applicationComponent: AppComponent
        get() = this.app.appComponent

    fun showToolbar() {
        supportActionBar?.show()
    }

    fun hideToolbar() {
        supportActionBar?.hide()
    }

    fun showBackButton(show: Boolean = true) {
        supportActionBar?.setHomeButtonEnabled(show)
     //   supportActionBar?.setDisplayHomeAsUpEnabled(show)
    }

    fun setToolbarTitle(toolbarTitle: String, subtitle: String? = "", backIcon: Boolean) {
        supportActionBar?.title = toolbarTitle
        supportActionBar?.subtitle = subtitle
      //  supportActionBar?.setDisplayHomeAsUpEnabled(backIcon)
    }

    fun setToolbarTitle(@StringRes toolbarTitleRes: Int, subtitle: String? = "") {
        supportActionBar?.title = getString(toolbarTitleRes)
        supportActionBar?.subtitle = subtitle
        supportActionBar?.setHomeButtonEnabled(true)
    }

    fun showHomeButton(){
        supportActionBar?.setHomeButtonEnabled(true)
    }

    fun showBackButton(){
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

}
package com.newletseduvate.base

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.newletseduvate.di.ViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.newletseduvate.app
import javax.inject.Inject

abstract class BaseBottomSheetFragment : BottomSheetDialogFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onAttach(context: Context) {
        super.onAttach(context)
      //  (activity as AppCompatActivity).app.appComponent.inject(this)
    }

    /**
     * @return A ViewModel tied to the lifecycle of the current fragment
     */
    protected inline fun <reified VM : ViewModel> getViewModel(): VM {
        return ViewModelProvider(this, viewModelFactory).get(VM::class.java)
    }

    /**
     * @return A ViewModel tied to the lifecycle of the specified fragment
     */
    protected inline fun <reified VM : ViewModel> getViewModel(fragment: Fragment): VM {
        return ViewModelProvider(fragment, viewModelFactory).get(VM::class.java)
    }

    /**
     * @return A ViewModel tied to the lifecycle of the specified activity
     */
    protected inline fun <reified VM : ViewModel> getViewModel(activity: AppCompatActivity): VM {
        return ViewModelProvider(activity, viewModelFactory).get(VM::class.java)
    }

}
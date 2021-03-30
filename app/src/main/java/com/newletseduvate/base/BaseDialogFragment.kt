package com.newletseduvate.base

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.newletseduvate.app
import com.newletseduvate.di.ViewModelFactory
import javax.inject.Inject

abstract class BaseDialogFragment : DialogFragment() {


    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as AppCompatActivity).app.appComponent.inject(this)
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

    protected inline fun <reified VM : ViewModel> getViewModel(activity: FragmentActivity): VM {
        return ViewModelProvider(activity, viewModelFactory).get(VM::class.java)
    }

}
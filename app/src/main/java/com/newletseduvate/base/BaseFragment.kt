package com.newletseduvate.base

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.newletseduvate.app
import com.newletseduvate.di.ViewModelFactory
import com.newletseduvate.ui.activities.MainActivity
import com.newletseduvate.ui.login.LoginActivity
import com.newletseduvate.utils.extensions.CustomProgressBar
import com.newletseduvate.utils.extensions.TermsAndConditions
import com.newletseduvate.utils.extensions.getToken
import javax.inject.Inject

abstract class BaseFragment(@LayoutRes private val layoutId: Int) : Fragment(layoutId) {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    lateinit var customProgressBar : CustomProgressBar
    lateinit var termsAndConditions: TermsAndConditions

    fun displayProgress(){
        customProgressBar.showProgressBar()

    }

    fun dismissProgress(){
        customProgressBar.dismissProgressBar()
    }

    fun showTermsAndConditions(){
        termsAndConditions.showTermsAndConditions()
    }

    fun dismissTermsAndConditions(){
        termsAndConditions.dismissTermsAndConditions()
    }

    fun getToken(): String{
        return sharedPreferences.getToken()!!
    }

    fun setLoginToolbar(title: String = ""){
        (activity as LoginActivity).supportActionBar?.title = title
    }

    fun setToolBarTitle(title: String = "") {
        (activity as MainActivity).supportActionBar?.title = title
    }

    fun showBackButton(show: Boolean = true) {
        (activity as MainActivity).supportActionBar?.setHomeButtonEnabled(show)
        (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(show)
    }

    /*fun hideKeypadMain(){
        (activity as MainActivity).window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    }

    fun hideKeypadLogin(){
        (activity as LoginActivity).window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    }*/

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as AppCompatActivity).app.appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        customProgressBar = CustomProgressBar.getInstance(requireContext())
        termsAndConditions = TermsAndConditions.getInstance(requireContext())
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

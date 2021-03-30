package com.newletseduvate.ui.forgotPassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.newletseduvate.R
import com.newletseduvate.base.BaseFragment
import com.newletseduvate.databinding.FragmentForgotPasswordBinding
import com.newletseduvate.databinding.FragmentLoginBinding
import com.newletseduvate.utils.Status
import com.newletseduvate.utils.HideKeypad
import com.newletseduvate.utils.NetworkCheck
import com.newletseduvate.utils.constants.DynamicUrl.LoginErpMapper
import com.newletseduvate.utils.extensions.CustomProgressBar
import com.newletseduvate.utils.extensions.snackBar
import com.newletseduvate.viewmodels.ForgotPasswordViewModel
import com.newletseduvate.viewmodels.LoginViewModel
import kotlinx.android.synthetic.main.fragment_forgot_password.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ForgotPasswordFragment : Fragment() {

//    private val viewModel by lazy { getViewModel<ForgotPasswordViewModel>() }

    val viewModel: ForgotPasswordViewModel by viewModels()

    lateinit var binding : FragmentForgotPasswordBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        clickEvents()
        attachLoginObserver()
    }

    private fun clickEvents(){
        binding.buttonForgotPassword.setOnClickListener {

            val userName = binding.editTextForgotPasswordErpcode.text.trim().toString().toUpperCase()

            val loginErpMapper = LoginErpMapper
            loginErpMapper["OLV"] = "https://dev.olvorchidnaigaon.letseduvate.com/qbox/"

            HideKeypad.hide(requireContext(), requireView())
            if(userName.isNotEmpty()){
                GlobalScope.launch {
                    if(NetworkCheck.isInternetAvailable(requireContext())){

                        val baseUrl = loginErpMapper.getOrDefault(userName.takeLast(3), "https://dev.olvorchidnaigaon.letseduvate.com/qbox/")
                        viewModel.getForgotPassword(userName, baseUrl)
                    }else{
                        withContext(Dispatchers.Main) {
                            layout_forgot_password.snackBar(resources.getString(R.string.connect_internet))
                        }
                    }
                    withContext(Dispatchers.Main) {
                        CustomProgressBar.getInstance(requireContext()).showProgressBar()
                    }
                }
            }else{
                layout_forgot_password.snackBar(resources.getString(R.string.enter_erp_code))
            }
        }
    }

    private fun attachLoginObserver() {
        viewModel.forgotPasswordResponse.observe(viewLifecycleOwner, {
            GlobalScope.launch {
                withContext(Dispatchers.Main) {
                    CustomProgressBar.getInstance(requireContext()).dismissProgressBar()
                }
            }
            it?.let {
                GlobalScope.launch {
                    withContext(Dispatchers.Main) {
                        when(it.status){
                            Status.Success -> {
                                layout_forgot_password.snackBar(it.data?.message!!)
                                findNavController().navigate(R.id.nav_login)
                            }
                            Status.Error -> {
                                layout_forgot_password.snackBar(it.message!!)
                            }
                            else -> {
                                layout_forgot_password.snackBar(it.message!!)
                            }
                        }
                    }
                }
            }
        })
    }
}
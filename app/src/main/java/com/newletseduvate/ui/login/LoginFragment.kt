package com.newletseduvate.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.newletseduvate.R
import com.newletseduvate.databinding.FragmentLoginBinding
import com.newletseduvate.utils.Status
import com.newletseduvate.model.login.LoginRequest
import com.newletseduvate.utils.HideKeypad
import com.newletseduvate.utils.NetworkCheck
import com.newletseduvate.utils.constants.DynamicUrl
import com.newletseduvate.utils.extensions.setBaseUrl
import com.newletseduvate.utils.extensions.snackBar
import com.newletseduvate.viewmodels.LoginViewModel
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginFragment : Fragment() {

//    private val viewModel by lazy { getViewModel<LoginViewModel>() }

    val viewModel: LoginViewModel by viewModels()
    lateinit var binding : FragmentLoginBinding

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()

        binding.checkBoxRememberPassword.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                viewModel.setRememberMe(1)
            }else{
                viewModel.setRememberMe(0)
            }
        }

        if(viewModel.getRememberMe() == 1){
            binding.editTextLoginUsername.setText(viewModel.getUserNameLogin())
            binding.editTextLoginPassword.setText(viewModel.getPasswordLogin())
        }
    }

    private fun init() {
        setupViews()
        attachLoginObserver()
    }

    private fun setupViews() {

        //   editText_login_username.setText("2103450003_MIT")
        //   editText_login_password.setText("2103450003_MIT")

        val loginErpMapper = DynamicUrl.LoginErpMapper
        loginErpMapper["OLV"] = "https://dev.olvorchidnaigaon.letseduvate.com/qbox/"

        binding.buttonLogin.setOnClickListener {

            val userName : Any = binding.editTextLoginUsername.text.trim().toString().toUpperCase()
            val password = binding.editTextLoginPassword.text.trim().toString()

            HideKeypad.hide(requireContext(), requireView())
            if (userName.toString().isNotEmpty()) {
                if (password.isNotEmpty()) {
                    GlobalScope.launch {
                        if (NetworkCheck.isInternetAvailable(requireContext())) {

                            val baseUrl = loginErpMapper.getOrDefault(userName.toString().takeLast(3), "https://dev.olvorchidnaigaon.letseduvate.com/qbox/")
                            val pref = PreferenceManager.getDefaultSharedPreferences(context)
                            pref.setBaseUrl(baseUrl)

                            viewModel.loginUser(LoginRequest(binding.editTextLoginUsername.text.trim().toString(),
                                binding.editTextLoginPassword.text.trim().toString()), baseUrl)

                        } else {
                            withContext(Dispatchers.Main) {
                                layout_login.snackBar(resources.getString(R.string.connect_internet))
                            }
                        }
                        withContext(Dispatchers.Main) {
//                            displayProgress()
                        }
                    }
                } else {
                    binding.root.snackBar(resources.getString(R.string.enter_password))
                }
            } else {
                binding.root.snackBar(resources.getString(R.string.enter_username))
            }
        }

        binding.tvLoginForgotPassword.setOnClickListener {
            findNavController().navigate(R.id.nav_forgot_password)
        }
    }

    private fun attachLoginObserver() {
        viewModel.data.observe(viewLifecycleOwner, {
            GlobalScope.launch {
                withContext(Dispatchers.Main) {
//                    dismissProgress()
                }
            }
            it?.let {
                GlobalScope.launch {
                    withContext(Dispatchers.Main) {
                        when (it.status) {
                            Status.Success -> {
                                setSuccessAndClose()
                            }
                            Status.Error -> {
                                layout_login.snackBar(it.message!!)
                            }
                            else -> {
                                layout_login.snackBar(it.message!!)
                            }
                        }
                    }
                }
            }
        })
    }

    private fun setSuccessAndClose() {
        (activity as? LoginActivity)?.setSuccess()
    }
}
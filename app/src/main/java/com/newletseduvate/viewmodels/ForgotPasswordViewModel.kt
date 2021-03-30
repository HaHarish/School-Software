package com.newletseduvate.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.newletseduvate.utils.Resource
import com.newletseduvate.model.forgotPassword.ForgotPasswordResponse
import com.newletseduvate.repository.ForgotPasswordRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ForgotPasswordViewModel(application: Application) : AndroidViewModel(application){

    var forgotPasswordResponse = MutableLiveData<Resource<ForgotPasswordResponse>>()

    val forgotPasswordRepository = ForgotPasswordRepository(application)

    fun getForgotPassword(erpId: String, baseUrl: String){
        GlobalScope.launch {
            val response = forgotPasswordRepository.getForgotPassword(erpId, baseUrl)
            withContext(Dispatchers.Main){
                when(response.body()?.statusCode){
                    200 -> {
                        forgotPasswordResponse.value = response.body()?.let { Resource.success(it) }
                    }
                    else -> forgotPasswordResponse.value = response.body()?.message?.let { Resource.error(it) }
                }
            }
        }
    }
}
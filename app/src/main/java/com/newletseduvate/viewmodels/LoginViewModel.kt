package com.newletseduvate.viewmodels

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.preference.PreferenceManager
import com.google.gson.Gson
import com.newletseduvate.utils.Resource
import com.newletseduvate.model.login.LoginRequest
import com.newletseduvate.model.login.LoginSuccessResponse
import com.newletseduvate.repository.LoginRepository
import com.newletseduvate.utils.constants.PrefKeys
import com.newletseduvate.utils.extensions.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(application)
    private val loginRepository = LoginRepository(application)

    val data = MutableLiveData<Resource<LoginSuccessResponse>>()

    fun setRememberMe(value: Int){
        sharedPreferences.setRememberMe(value)
    }

    fun getRememberMe(): Int{
        return sharedPreferences.getInt(PrefKeys.REMEMBER_ME, 0)
    }

    fun getUserNameLogin(): String{
        return sharedPreferences.getString(PrefKeys.USER_NAME_LOGIN, null).toString()
    }

    fun getPasswordLogin(): String{
        return sharedPreferences.getString(PrefKeys.USER_PASSWORD_LOGIN, null).toString()
    }

    fun loginUser(loginRequest: LoginRequest, baseUrl: String){
        GlobalScope.launch {
            val response = loginRepository.loginUser(loginRequest,baseUrl)
            withContext(Dispatchers.Main){
                when(response.body()?.statusCode){
                    200 -> {
                        val gson = Gson()
                        val responseString = gson.toJson(response.body())
                        sharedPreferences.setLoginResponse(responseString)
                        sharedPreferences.setPassword(loginRequest.password)
                        sharedPreferences.setIsUserLoggedIn(true)

                        sharedPreferences.setUsernameLogin(loginRequest.username!!)
                        sharedPreferences.setPasswordLogin(loginRequest.password!!)

                        response.body()?.result?.userDetails?.userId?.let {
                            sharedPreferences.setUserId(it)
                        }
                        response.body()?.result?.userDetails?.firstName?.let {
                            sharedPreferences.setUserFirstName(it)
                        }
                        response.body()?.result?.userDetails?.lastName?.let {
                            sharedPreferences.setUserLastName(it)
                        }
                        response.body()?.result?.userDetails?.token?.let {
                            sharedPreferences.setToken(it)
                        }
                        response.body()?.result?.userDetails?.isSuperuser?.let {
                            sharedPreferences.setIsSuperUser(it)
                        }
                        response.body()?.result?.userDetails?.userId?.let {
                            sharedPreferences.setUserIdNew(it.toString())
                        }
                        response.body()?.result?.userDetails?.roleDetails?.erpUserId?.let {
                            sharedPreferences.setErpUserId(it)
                        }

                        response.body()?.result?.userDetails?.roleDetails?.roleId?.let {
                            sharedPreferences.setRoleId(it)
                        }

                        response.body()?.result?.userDetails?.roleDetails?.branch?.get(0)?.branchName.let {
                            if (it != null) {
                                sharedPreferences.setBranchName(it)
                            }
                        }

                        data.value = response.body()?.let { Resource.success(it) }
                    }
                    401 -> data.value = response.body()?.message?.let { Resource.error(it) }
                    else -> data.value = Resource.error("Something went wrong")
                }
            }
        }
    }
}
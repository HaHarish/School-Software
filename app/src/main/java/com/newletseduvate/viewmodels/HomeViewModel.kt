package com.newletseduvate.viewmodels

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.newletseduvate.utils.Resource
import com.newletseduvate.model.general.UploadFileModel
import com.newletseduvate.model.login.LoginSuccessResponse
import com.newletseduvate.model.profile.ChangePasswordRequest
import com.newletseduvate.model.profile.ChangePasswordResponse
import com.newletseduvate.model.profile.ChangeProfilePictureResponse
import com.newletseduvate.model.profile.ProfileDetailsResponse
import com.newletseduvate.repository.ProfileRepository
import com.newletseduvate.utils.extensions.getLoginResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HomeViewModel @Inject constructor(private val sharedPreferences: SharedPreferences,
                                        private val profileRepository: ProfileRepository
): ViewModel() {

    val data = MutableLiveData<LoginSuccessResponse>()

    fun getUserDetails(){
        data.value = Gson().fromJson(sharedPreferences.getLoginResponse(), LoginSuccessResponse::class.java)
    }

    val changeProfilePicture = MutableLiveData<Resource<ChangeProfilePictureResponse>>()
    val getProfileDetailsResponse = MutableLiveData<Resource<ProfileDetailsResponse>>()
    val changePasswordResponse = MutableLiveData<Resource<ChangePasswordResponse>>()

    fun changeProfilePicture(token: String,
                                  profile: UploadFileModel,
                                  userId: String){
        GlobalScope.launch {
            val response = profileRepository.changeProfilePicture(token,profile,userId)
            withContext(Dispatchers.Main){
                when(response.body()?.statusCode){
                    200 -> {
                        changeProfilePicture.value = Resource.success(response.body())
                    }
                    401 -> changeProfilePicture.value = response.body()?.message?.let { Resource.error(it) }
                    else -> changeProfilePicture.value = Resource.error("Something went wrong")
                }
            }
        }
    }

    fun getProfileDetails(){
        GlobalScope.launch {
            val response = profileRepository.getProfileDetails()
            withContext(Dispatchers.Main){
                when(response.body()?.statusCode){
                    200 -> {
                        getProfileDetailsResponse.value = Resource.success(response.body())
                    }
                    401 -> getProfileDetailsResponse.value = response.body()?.message?.let { Resource.error(it) }
                    else -> getProfileDetailsResponse.value = Resource.error("Something went wrong")
                }
            }
        }
    }

    fun changePassword(changePasswordRequest: ChangePasswordRequest){
        GlobalScope.launch {
            val response = profileRepository.changePassword(changePasswordRequest)
            withContext(Dispatchers.Main){
                when(response.body()?.statusCode){
                    200 -> {
                        changePasswordResponse.value = Resource.success(response.body())
                    }
                    401 -> changePasswordResponse.value = response.body()?.message?.let { Resource.error(it) }
                    else -> changePasswordResponse.value = Resource.error("Something went wrong")
                }
            }
        }
    }
}
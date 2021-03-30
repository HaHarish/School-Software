package com.newletseduvate.repository

import android.content.SharedPreferences
import android.util.Log
import com.newletseduvate.api.LoginServices
import com.newletseduvate.api.OnlineClassServices
import com.newletseduvate.api.ProfileService
import com.newletseduvate.model.general.UploadFileModel
import com.newletseduvate.model.profile.ChangePasswordRequest
import com.newletseduvate.model.profile.ChangePasswordResponse
import com.newletseduvate.model.profile.ChangeProfilePictureResponse
import com.newletseduvate.model.profile.ProfileDetailsResponse
import com.newletseduvate.utils.extensions.getErpUserId
import com.newletseduvate.utils.extensions.getToken
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileRepository @Inject constructor(private val apiService: ProfileService,
                                            private val sharedPreferences: SharedPreferences) {

    /*lateinit var apiService: ProfileService

    init {

        val loggingInterceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                Log.d("OkHttp",message)
            }
        })
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val builder = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .retryOnConnectionFailure(true)
            .readTimeout(3, TimeUnit.MINUTES)
            .connectTimeout(3, TimeUnit.MINUTES)
            .callTimeout(3, TimeUnit.MINUTES)
            .writeTimeout(3, TimeUnit.MINUTES)

        val variable = sharedPreferences.getUrlValue()

        val retrofit = Retrofit.Builder()
            .baseUrl(variable)
            .client(builder.build())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(LoginServices::class.java)

        apiService = retrofit.create(ProfileService::class.java)
    }*/

    /* Change Profile Picture */
    suspend fun changeProfilePicture(token: String,
                                     profile: UploadFileModel,
                                     userId: String): Response<ChangeProfilePictureResponse> {

        val requestFile = RequestBody.create(
            "multipart/form-data".toMediaTypeOrNull(),
            profile.file
        )

        return apiService.changeProfilePicture(token,
            MultipartBody.Part.createFormData(
                "profile",
                "photo-"+System.currentTimeMillis().toString()+ profile.file.name,
                requestFile
            ),
            userId)
    }

    /* Get Profile Details */
    suspend fun getProfileDetails(): Response<ProfileDetailsResponse> {
        return apiService.getProfileDetails(sharedPreferences.getToken()!!,
                                            sharedPreferences.getErpUserId())
    }

    /* Change Password */
    suspend fun changePassword(changePasswordRequest: ChangePasswordRequest): Response<ChangePasswordResponse> {
        return apiService.changePassword(sharedPreferences.getToken()!!,
            sharedPreferences.getErpUserId(),
            changePasswordRequest)
    }
}
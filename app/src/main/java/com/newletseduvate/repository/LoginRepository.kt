package com.newletseduvate.repository

import android.app.Application
import android.util.Log
import com.newletseduvate.api.LoginServices
import com.newletseduvate.model.login.LoginRequest
import com.newletseduvate.model.login.LoginSuccessResponse
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit


class LoginRepository(application: Application) {

//    private val apiService: LoginServices



    suspend fun loginUser(loginRequest: LoginRequest, baseUrl: String): Response<LoginSuccessResponse>{

        val loggingInterceptor = HttpLoggingInterceptor { message -> Log.d("OkHttp", message) }
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val builder = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .retryOnConnectionFailure(true)
                .readTimeout(3, TimeUnit.MINUTES)
                .connectTimeout(3, TimeUnit.MINUTES)
                .callTimeout(3, TimeUnit.MINUTES)
                .writeTimeout(3, TimeUnit.MINUTES)


        val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(builder.build())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build()
        val service = retrofit.create(LoginServices::class.java)
        return service.loginAsync(loginRequest)
    }
}
package com.newletseduvate.repository

import android.app.Application
import android.util.Log
import com.newletseduvate.api.ForgotPasswordService
import com.newletseduvate.api.LoginServices
import com.newletseduvate.model.forgotPassword.ForgotPasswordResponse
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

class ForgotPasswordRepository(private val application: Application) {
    suspend fun getForgotPassword(erpId: String, baseUrl: String): Response<ForgotPasswordResponse> {

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
        val service = retrofit.create(ForgotPasswordService::class.java)

        return service.getForgotPassword(erpId)
    }

}
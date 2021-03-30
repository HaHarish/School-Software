package com.newletseduvate.di

import android.app.Application
import android.util.Log
import com.google.gson.Gson
import com.newletseduvate.BuildConfig
import com.newletseduvate.api.*
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Suppress("TooManyFunctions")
@Module
class NetworkModule(private val baseUrl: String) {

    companion object {
        private const val API_ENDPOINT = BuildConfig.BASE_URL

        private const val READ_TIMEOUT_SEC = 15L
        private const val LONG_TIMEOUT_SEC = 180L
        private const val OKHTTP_LONG_TIMEOUT = "OKHTTP_LONG_TIMEOUT"
    }

    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                Log.d("OkHttp",message)
            }
        })
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return loggingInterceptor
    }

    @Provides
    fun provideGsonConverterFactory(gson: Gson): GsonConverterFactory {
        return GsonConverterFactory.create(gson)
    }

    @Singleton
    @Provides
    fun provideCache(application: Application): Cache {
        val cacheSize = 10 * 1024 * 1024 // 10 MB
        return Cache(application.cacheDir, cacheSize.toLong())
    }


    @Singleton
    @Provides
    fun provideOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .retryOnConnectionFailure(true)
            .readTimeout(3, TimeUnit.MINUTES)
            .connectTimeout(3, TimeUnit.MINUTES)
            .callTimeout(3, TimeUnit.MINUTES)
            .writeTimeout(3, TimeUnit.MINUTES)

        return builder.build()
    }

    @Provides
    fun provideRxJava2CallAdapterFactory(): RxJava2CallAdapterFactory =
        RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io())


    @Provides
    @Singleton
    fun providesRetrofit(
        okHttpClient: OkHttpClient,
        rxJava2CallAdapterFactory: RxJava2CallAdapterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(rxJava2CallAdapterFactory)
            .build()
    }

//    @Provides
//    @Singleton
//    fun provideLoginApi(retrofit: Retrofit): LoginServices {
//        return retrofit.create(LoginServices::class.java)
//    }

    @Provides
    @Singleton
    fun provideDiaryApi(retrofit: Retrofit): DiaryServices {
        return retrofit.create(DiaryServices::class.java)
    }

    @Provides
    @Singleton
    fun provideOnlineClassApi(retrofit: Retrofit): OnlineClassServices {
        return retrofit.create(OnlineClassServices::class.java)
    }

    @Provides
    @Singleton
    fun provideBlogApi(retrofit: Retrofit): BlogServices {
        return retrofit.create(BlogServices::class.java)
    }

    @Provides
    @Singleton
    fun provideHomeWorkApi(retrofit: Retrofit): HomeWorkService {
        return retrofit.create(HomeWorkService::class.java)
    }

    @Provides
    @Singleton
    fun provideLessonPlanApi(retrofit: Retrofit): LessonPlanService {
        return retrofit.create(LessonPlanService::class.java)
    }

    @Provides
    @Singleton
    fun provideProfileServiceApi(retrofit: Retrofit): ProfileService {
        return retrofit.create(ProfileService::class.java)
    }

    @Provides
    @Singleton
    fun provideCircularServiceApi(retrofit: Retrofit): CircularService {
        return retrofit.create(CircularService::class.java)
    }

    @Provides
    @Singleton
    fun provideFinanceServicesApi(retrofit: Retrofit): FinanceServices {
        return retrofit.create(FinanceServices::class.java)
    }



//    @Provides
//    @Singleton
//    fun provideForgotPasswordServiceApi(retrofit: Retrofit): ForgotPasswordService {
//        return retrofit.create(ForgotPasswordService::class.java)
//    }

    @Provides
    @Named("api_endpoint")
    fun provideApiEndpoint() = baseUrl
}

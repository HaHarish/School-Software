package com.newletseduvate.repository

import android.content.SharedPreferences
import android.util.Log
import com.google.gson.JsonObject
import com.newletseduvate.model.blog.PublishedBlogResponse
import com.newletseduvate.model.blog.StudentBlogCreateResponse
import com.newletseduvate.model.blog.StudentBlogGenreResponse
import com.newletseduvate.model.blog.StudentBlogResponse
import com.newletseduvate.model.general.UploadFileModel
import com.newletseduvate.api.BlogServices
import com.newletseduvate.api.LoginServices
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
class BlogRepository @Inject constructor(
    private val apiService: BlogServices,
    private val pref: SharedPreferences
) {
    /*lateinit var apiService: BlogServices

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

        val retrofit = Retrofit.Builder()
            .baseUrl(pref.getUrlValue())
            .client(builder.build())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(LoginServices::class.java)

        apiService = retrofit.create(BlogServices::class.java)
    }*/

    suspend fun getStudentBlog(
        moduleId: String,
        status: String,
        pageNumber: String
    ): Response<StudentBlogResponse> {
        return apiService.getStudentBlog(
            pref.getToken().toString(),
            moduleId,
            status,
            pageNumber
        )
    }

    suspend fun getPublishedBlog(
        moduleId: String,
        status: String,
        pageNumber: String,
        publishedLevel: String
    ): Response<PublishedBlogResponse> {
        return apiService.getPublishedBlog(
            pref.getToken().toString(),
            moduleId,
            status,
            pageNumber,
            publishedLevel
        )
    }

    suspend fun getStudentBlogGenre(): Response<StudentBlogGenreResponse> {
        return apiService.getStudentBlogGenre(
            pref.getToken().toString(),
            pref.getErpUserId().toString(),
        )
    }

    suspend fun postCreateBlog(
        status: String,
        title: String,
        content: String,
        wordCount: Int,
        genreId: Int,
        file: UploadFileModel
    ): Response<StudentBlogCreateResponse> {

        val requestFile = RequestBody.create(
            "multipart/form-data".toMediaTypeOrNull(),
            file.file
        )

        return apiService.postCreateBlog(
            pref.getToken().toString(),
            RequestBody.create("text/plain".toMediaTypeOrNull(), status),
            RequestBody.create("text/plain".toMediaTypeOrNull(), title),
            RequestBody.create("text/plain".toMediaTypeOrNull(), content),
            RequestBody.create("text/plain".toMediaTypeOrNull(), wordCount.toString()),
            RequestBody.create("text/plain".toMediaTypeOrNull(), genreId.toString()),
            MultipartBody.Part.createFormData(
                "thumbnail",
                "photo-"+System.currentTimeMillis().toString()+ file.file.name,
                requestFile
            )
        )
    }

    suspend fun putDeleteBlog(
        blogId: Int
    ): Response<JsonObject> {

        val body = JsonObject()
        body.addProperty("blog_id", blogId)
        body.addProperty("status", "1")

        return apiService.putDeleteBlog(
            pref.getToken().toString(), body
        )
    }

    suspend fun putRestoreBlog(
        blogId: Int
    ): Response<JsonObject> {

        val body = JsonObject()
        body.addProperty("blog_id", blogId)
        body.addProperty("status", "9")

        return apiService.putRestoreBlog(
            pref.getToken().toString(), body
        )
    }

}
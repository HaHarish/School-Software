package com.newletseduvate.api

import com.google.gson.JsonObject
import com.newletseduvate.model.blog.PublishedBlogResponse
import com.newletseduvate.model.blog.StudentBlogCreateResponse
import com.newletseduvate.model.blog.StudentBlogGenreResponse
import com.newletseduvate.model.blog.StudentBlogResponse
import com.newletseduvate.utils.constants.ApiConstants
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

/**
 * Created by SHASHANK BHAT on 19-Feb-21.
 *
 *
 */
interface BlogServices {

    @GET(ApiConstants.studentBlogUrl)
    suspend fun getStudentBlog(
        @Header(ApiConstants.AUTHORIZATION) token: String,
        @Query("module_id") moduleId: String,
        @Query("status") status: String,
        @Query("page_number") pageNumber: String
    ): Response<StudentBlogResponse>

    @GET(ApiConstants.publishedBlogUrl)
    suspend fun getPublishedBlog(
        @Header(ApiConstants.AUTHORIZATION) token: String,
        @Query("module_id") moduleId: String,
        @Query("status") status: String,
        @Query("page_number") pageNumber: String,
        @Query("published_level") publishedLevel: String
    ): Response<PublishedBlogResponse>


    @GET(ApiConstants.studentBlogGenreUrl)
    suspend fun getStudentBlogGenre(
        @Header(ApiConstants.AUTHORIZATION) token: String,
        @Query("erp_user_id") erp_user_id: String,
    ): Response<StudentBlogGenreResponse>

    @POST(ApiConstants.studentBlogCreateUrl)
    @Multipart
    suspend fun postCreateBlog(
        @Header(ApiConstants.AUTHORIZATION) token: String,
        @Part ("status") status: RequestBody,
        @Part ("title") title: RequestBody,
        @Part ("content") content: RequestBody,
        @Part ("word_count") word_count: RequestBody,
        @Part ("genre_id") genre_id: RequestBody,
        @Part thumbnail: MultipartBody.Part,
    ): Response<StudentBlogCreateResponse>


    @PUT(ApiConstants.studentBlogCreateUrl)
    suspend fun putDeleteBlog(
        @Header(ApiConstants.AUTHORIZATION) token: String,
        @Body body: JsonObject
    ): Response<JsonObject>

    @PUT(ApiConstants.studentBlogCreateUrl)
    suspend fun putRestoreBlog(
        @Header(ApiConstants.AUTHORIZATION) token: String,
        @Body body: JsonObject
    ): Response<JsonObject>
}
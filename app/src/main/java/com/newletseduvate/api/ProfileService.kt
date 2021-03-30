package com.newletseduvate.api

import com.newletseduvate.model.blog.StudentBlogCreateResponse
import com.newletseduvate.model.profile.ChangePasswordRequest
import com.newletseduvate.model.profile.ChangePasswordResponse
import com.newletseduvate.model.profile.ChangeProfilePictureResponse
import com.newletseduvate.model.profile.ProfileDetailsResponse
import com.newletseduvate.utils.constants.ApiConstants
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface ProfileService {

    @PUT(ApiConstants.changeProfilePicture)
    @Multipart
    suspend fun changeProfilePicture(
        @Header(ApiConstants.AUTHORIZATION) token: String,
        @Part profile: MultipartBody.Part,
        @Path ("userId") userId: String
    ): Response<ChangeProfilePictureResponse>

    @GET(ApiConstants.getProfileDetails)
    suspend fun getProfileDetails(
        @Header(ApiConstants.AUTHORIZATION) token: String,
        @Query ("erp_user_id") erpUserId: Int
    ): Response<ProfileDetailsResponse>

    @PUT(ApiConstants.changePasswordUrl)
    suspend fun changePassword(
        @Header(ApiConstants.AUTHORIZATION) token: String,
        @Path ("userId") userId: Int,
        @Body changePasswordRequest: ChangePasswordRequest
    ): Response<ChangePasswordResponse>

}
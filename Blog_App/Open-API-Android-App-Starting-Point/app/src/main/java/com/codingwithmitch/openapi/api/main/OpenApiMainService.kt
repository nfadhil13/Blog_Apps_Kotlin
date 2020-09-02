package com.codingwithmitch.openapi.api.main

import androidx.lifecycle.LiveData
import com.codingwithmitch.openapi.api.GenericResponse
import com.codingwithmitch.openapi.api.main.responses.BlogListSearchResponse
import com.codingwithmitch.openapi.dependcy_injection.main.MainScope
import com.codingwithmitch.openapi.models.AccountProperties
import retrofit2.http.*

@MainScope
interface OpenApiMainService{

    @GET("account/properties")
    suspend fun getAccountProperties(
        @Header("Authorization") authorization : String
    ) : AccountProperties


    @PUT("account/properties/update")
    @FormUrlEncoded
    suspend fun saveAccountProperties(
        @Header("Authorization") authorization: String,
        @Field("email") email : String,
        @Field("username") username : String
    ): GenericResponse


    @PUT("account/change_password/")
    @FormUrlEncoded
    suspend fun updatePassword(
        @Header("Authorization") authorization: String,
        @Field("old_password") currentPassword : String,
        @Field("new_password") newPassword: String,
        @Field("confirm_new_password") confirmNewPassword : String
    ): GenericResponse


    @GET("blog/list")
    suspend fun searchListBlogPost(
        @Header("Authorization") authorization: String,
        @Query("search") query : String,
        @Query("ordering") ordering : String,
        @Query("page") page : Int
    ) : BlogListSearchResponse

    @GET("blog/list")
    suspend fun searchListBlogPosts()

}
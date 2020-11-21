package com.yapp.picon.data.api

import com.yapp.picon.data.model.*
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.http.*

interface YappApi {

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("auth/signIn")
    suspend fun simpleJoin(
        @Body simpleJoinRequest: SimpleJoinRequest
    ): SimpleJoinResponse

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("auth/logIn")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): LoginResponse

    @Multipart
    @POST("display/post/images")
    suspend fun uploadImage(
        @Header("AccessToken") accessToken: String,
        @Part("images") parts: List<MultipartBody.Part>
    ): ResponseBody

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("/display/post")
    suspend fun createPost(
        @Header("AccessToken") accessToken: String,
        @Body postRequest: PostRequest
    ): ResponseBody

//    @GET("displays/post/{id}")
//    suspend fun requestPost(
//        @Path("id") id: String
//    ): Post

}
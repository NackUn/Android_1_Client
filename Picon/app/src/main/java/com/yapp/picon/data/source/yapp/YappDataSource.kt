package com.yapp.picon.data.source.yapp

import com.yapp.picon.data.model.*
import okhttp3.MultipartBody
import okhttp3.ResponseBody

interface YappDataSource {
    suspend fun simpleJoin(simpleJoinRequest: SimpleJoinRequest): SimpleJoinResponse
    suspend fun login(loginRequest: LoginRequest): LoginResponse
    suspend fun uploadImage(accessToken: String, parts: List<MultipartBody.Part>): ResponseBody
    suspend fun createPost(accessToken: String, postRequest: PostRequest): ResponseBody
}
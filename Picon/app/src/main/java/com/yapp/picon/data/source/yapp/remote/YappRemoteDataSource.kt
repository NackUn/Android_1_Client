package com.yapp.picon.data.source.yapp.remote

import com.yapp.picon.data.api.YappApi
import com.yapp.picon.data.model.*
import com.yapp.picon.data.source.yapp.YappDataSource
import okhttp3.MultipartBody
import okhttp3.ResponseBody

class YappRemoteDataSource(
    private val retrofitService: YappApi
) : YappDataSource {
    override suspend fun simpleJoin(simpleJoinRequest: SimpleJoinRequest): SimpleJoinResponse =
        retrofitService.simpleJoin(simpleJoinRequest)

    override suspend fun login(loginRequest: LoginRequest): LoginResponse =
        retrofitService.login(loginRequest)

    override suspend fun uploadImage(
        accessToken: String,
        parts: List<MultipartBody.Part>
    ): ResponseBody =
        retrofitService.uploadImage(accessToken, parts)

    override suspend fun createPost(accessToken: String, postRequest: PostRequest): ResponseBody =
        retrofitService.createPost(accessToken, postRequest)
}
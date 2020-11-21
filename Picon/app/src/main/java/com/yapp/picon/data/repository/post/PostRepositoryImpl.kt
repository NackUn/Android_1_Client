package com.yapp.picon.data.repository.post

import com.yapp.picon.data.model.PostRequest
import com.yapp.picon.data.source.yapp.YappDataSource
import okhttp3.MultipartBody
import okhttp3.ResponseBody

class PostRepositoryImpl(
    private val yappDataSource: YappDataSource
) : PostRepository {
    override suspend fun uploadImage(
        accessToken: String,
        parts: List<MultipartBody.Part>
    ): ResponseBody =
        yappDataSource.uploadImage(accessToken, parts)

    override suspend fun createPost(accessToken: String, postRequest: PostRequest): ResponseBody =
        yappDataSource.createPost(accessToken, postRequest)
}
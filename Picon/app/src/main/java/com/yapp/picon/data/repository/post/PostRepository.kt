package com.yapp.picon.data.repository.post

import com.yapp.picon.data.model.PostRequest
import okhttp3.MultipartBody
import okhttp3.ResponseBody

interface PostRepository {
    suspend fun uploadImage(accessToken: String, parts: List<MultipartBody.Part>): ResponseBody
    suspend fun createPost(accessToken: String, postRequest: PostRequest): ResponseBody
}
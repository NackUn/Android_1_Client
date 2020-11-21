package com.yapp.picon.domain.usecase

import com.yapp.picon.data.repository.post.PostRepository
import okhttp3.MultipartBody
import okhttp3.ResponseBody

class UploadImageUseCase(
    private val postRepository: PostRepository
) {
    suspend operator fun invoke(
        accessToken: String,
        parts: List<MultipartBody.Part>
    ): ResponseBody =
        postRepository.uploadImage(accessToken, parts)
}
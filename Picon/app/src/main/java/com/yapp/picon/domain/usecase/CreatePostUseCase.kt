package com.yapp.picon.domain.usecase

import com.yapp.picon.data.model.Address
import com.yapp.picon.data.model.Coordinate
import com.yapp.picon.data.model.Post
import com.yapp.picon.data.model.PostRequest
import com.yapp.picon.data.repository.post.PostRepository
import okhttp3.ResponseBody

class CreatePostUseCase(
    private val postRepository: PostRepository
) {
    suspend operator fun invoke(
        accessToken: String,
        coordinate: Coordinate,
        imageUrls: List<String>?,
        address: Address,
        emotion: String?,
        memo: String?
    ): ResponseBody =
        postRepository.createPost(
            accessToken, PostRequest(
                Post(
                    id = 10,
                    coordinate = coordinate,
                    imageUrls = imageUrls,
                    address = address,
                    emotion = emotion,
                    memo = memo
                )
            )
        )
}
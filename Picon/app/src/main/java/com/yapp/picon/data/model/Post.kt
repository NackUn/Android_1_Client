package com.yapp.picon.data.model

data class Post(
    val id: Int?,
    val coordinate: Coordinate,
    val imageUrls: List<String>?,
    val address: Address,
    val emotion: Emotion?,
    val memo: String?,
    val createdDate: String?
)
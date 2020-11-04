package com.yapp.picon.data.model

data class Post(
    val id: Int,
    val coordinate: Coordinate,
    val address: Address,
    val emotion: String,
    val memo: String,
    val createDate: String?
)
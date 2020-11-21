package com.yapp.picon.data.model

data class PostResponse(
    override val status: Int,
    override val errors: String,
    override val errorCode: String,
    override val errorMessage: String,
    val id: Int,
    val coordinate: Coordinate,
    val address: Address,
    val emotion: String,
    val memo: String,
    val createDate: String?
) : Response()
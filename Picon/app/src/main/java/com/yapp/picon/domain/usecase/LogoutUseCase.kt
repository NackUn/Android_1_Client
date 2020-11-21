package com.yapp.picon.domain.usecase

import com.yapp.picon.data.repository.user.UserRepository

class LogoutUseCase(private val repository: UserRepository) {
    suspend operator fun invoke() =
        repository.saveAccessToken("")
}
package com.eldebvs.consulting.domain.use_case.auth_use_case

import com.eldebvs.consulting.domain.repository.AuthRepository

class ResetUserPassword(
    private val repository: AuthRepository
) {
    suspend operator fun invoke() = repository.resetUserPassword()
}
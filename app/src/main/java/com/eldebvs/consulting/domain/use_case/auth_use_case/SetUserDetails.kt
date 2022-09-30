package com.eldebvs.consulting.domain.use_case.auth_use_case

import com.eldebvs.consulting.domain.repository.AuthRepository

class SetUserDetails(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(name: String) = repository.setUserDetails(name)
}
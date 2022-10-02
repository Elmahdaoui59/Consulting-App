package com.eldebvs.consulting.domain.use_case.auth_use_case

import com.eldebvs.consulting.domain.repository.AuthRepository

class EditUserDetails(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(name: String?, phone: String?) = repository.editUserDetails(name, phone)
}
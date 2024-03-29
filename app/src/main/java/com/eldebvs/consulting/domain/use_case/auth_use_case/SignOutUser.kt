package com.eldebvs.consulting.domain.use_case.auth_use_case

import com.eldebvs.consulting.domain.repository.AuthRepository

class SignOutUser(
    private val repository: AuthRepository
) {
    operator fun invoke() = repository.signOutUser()
}
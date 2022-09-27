package com.eldebvs.consulting.domain.use_case.auth_use_case

import com.eldebvs.consulting.domain.repository.AuthRepository

class SignInUser(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String) =
        repository.signInUser(email, password)
}
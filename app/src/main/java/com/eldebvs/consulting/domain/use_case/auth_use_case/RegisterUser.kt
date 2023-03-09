package com.eldebvs.consulting.domain.use_case.auth_use_case

import com.eldebvs.consulting.domain.repository.AuthRepository

class RegisterUser(
    private val repository: AuthRepository
) {
    operator fun invoke(email: String, password:String) = repository.registerUser(email, password)
}
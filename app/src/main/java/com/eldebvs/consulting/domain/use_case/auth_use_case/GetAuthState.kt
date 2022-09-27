package com.eldebvs.consulting.domain.use_case.auth_use_case

import com.eldebvs.consulting.domain.repository.AuthRepository

class GetAuthState(
    private val repository: AuthRepository
) {
    operator fun invoke() = repository.getFirebaseAuthState()
}
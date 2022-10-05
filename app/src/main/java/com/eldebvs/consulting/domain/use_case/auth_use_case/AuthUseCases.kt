package com.eldebvs.consulting.domain.use_case.auth_use_case

import com.eldebvs.consulting.domain.use_case.settings_use_case.ResetUserPassword

data class AuthUseCases(
    val registerUser: RegisterUser,
    val signOutUser: SignOutUser,
    val signInUser: SignInUser,
    val getAuthState: GetAuthState,
    val resendVerificationEmail: ResendVerificationEmail,
)
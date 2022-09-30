package com.eldebvs.consulting.domain.use_case.auth_use_case

data class AuthUseCases(
    val registerUser: RegisterUser,
    val signOutUser: SignOutUser,
    val signInUser: SignInUser,
    val getAuthState: GetAuthState,
    val resendVerificationEmail: ResendVerificationEmail,
    val getUserDetails: GetUserDetails,
    val setUserDetails: SetUserDetails,
    val resetUserPassword: ResetUserPassword,
    val editUserEmail: EditUserEmail
)
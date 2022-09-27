package com.eldebvs.consulting.presentation.auth

import androidx.annotation.StringRes
import com.eldebvs.consulting.R
import com.eldebvs.consulting.domain.model.Response

data class AuthenticationState(
    val email: String? = null,
    val password: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val passwordRequirements: List<PasswordRequirement> = emptyList(),
    val authenticationMode: AuthenticationMode = AuthenticationMode.SIGN_IN,
    val isUserAuthenticated: Boolean = false,
    val showResetEmailDialog: Boolean = false
) {
    fun isFormValid(): Boolean? {
        return password?.isNotEmpty() == true
                && email?.isNotEmpty() == true
                && (authenticationMode== AuthenticationMode.SIGN_IN
                || passwordRequirements.containsAll(PasswordRequirement.values().toList()))
    }
}

enum class AuthenticationMode {
    SIGN_IN, SIGN_UP
}

enum class PasswordRequirement(
    @StringRes val label: Int
) {
    CAPITAL_LETTER(R.string.password_requirements_capital),
    NUMBER(R.string.password_requirements_digit),
    EIGHT_CHARACTERS(R.string.password_requirements_characters)
}
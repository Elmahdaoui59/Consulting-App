package com.eldebvs.consulting.presentation.settings

data class UserDetails(
    val name: String? = null,
    val phone: String? = null,
    val email: String? = null,
    val password: String? = null
) {
    fun isSettingFormValid(): Boolean {
        val checkDetailsChanged: Boolean = (!name.isNullOrEmpty() || !phone.isNullOrEmpty())
        return if (!email.isNullOrEmpty()) {
            !password.isNullOrEmpty()
        } else {
            checkDetailsChanged
        }
    }
}

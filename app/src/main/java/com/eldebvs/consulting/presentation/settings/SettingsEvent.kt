package com.eldebvs.consulting.presentation.settings


sealed class SettingsEvent {
    class NameChanged( val name: String): SettingsEvent()
    class PhoneChanged(val phone: String): SettingsEvent()
    class EmailChanged(val email: String): SettingsEvent()
    class PasswordChanged(val password: String): SettingsEvent()
    object ResetUserPassword: SettingsEvent()
    object EditUserDetails: SettingsEvent()
}

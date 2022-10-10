package com.eldebvs.consulting.presentation.settings

import android.net.Uri
import com.eldebvs.consulting.presentation.settings.components.PhotoSource


sealed class SettingsEvent {
    class NameChanged( val name: String): SettingsEvent()
    class PhoneChanged(val phone: String): SettingsEvent()
    class EmailChanged(val email: String): SettingsEvent()
    class PasswordChanged(val password: String): SettingsEvent()
    object ResetUserPassword: SettingsEvent()
    object EditUserDetails: SettingsEvent()
    object ChangeProfilePhoto : SettingsEvent()
    object ErrorDismissed: SettingsEvent()
    object DismissUploadPhotoDialog: SettingsEvent()
    class UploadOptionChosen(val photoSource: PhotoSource): SettingsEvent()
    class GetLocalProfilePhotoUri(val uri: Uri): SettingsEvent()
    object EnableCompression: SettingsEvent()
    object DisableCompression: SettingsEvent()
    object GetUserDetails: SettingsEvent()
}

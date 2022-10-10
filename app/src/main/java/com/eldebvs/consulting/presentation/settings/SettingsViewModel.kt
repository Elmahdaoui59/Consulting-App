package com.eldebvs.consulting.presentation.settings

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eldebvs.consulting.R
import com.eldebvs.consulting.domain.model.Response
import com.eldebvs.consulting.domain.use_case.settings_use_case.GetUserDetails
import com.eldebvs.consulting.domain.use_case.settings_use_case.SettingsUsesCases
import com.eldebvs.consulting.presentation.common.UiEvent
import com.eldebvs.consulting.presentation.settings.components.PhotoSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsUsesCases: SettingsUsesCases,
) : ViewModel() {

    private val _userDetails = MutableStateFlow(UserDetails())
    val userDetails = _userDetails.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _settingUiState = MutableStateFlow(SettingsUiState())
    val settingsUiState = _settingUiState.asStateFlow()


    init {
        getUserDetails()
    }


    private fun getUserDetails() {
        viewModelScope.launch {
            settingsUsesCases.getUserDetails().collect { response ->
                when (response) {
                    is Response.Failure -> {
                        _settingUiState.update {
                            it.copy(
                                error = response.e.message,
                                isLoading = false,
                            )
                        }
                    }
                    is Response.Loading -> {
                        _settingUiState.update {
                            it.copy(
                                isLoading = true
                            )
                        }
                    }
                    is Response.Success -> {
                        _userDetails.update {
                            it.copy(
                                name = response.data?.name,
                                phone = response.data?.phone,
                                email = response.data?.email,
                                profile_photo_firebase_url = response.data?.profile_image
                            )
                        }
                        Log.d("user details updated", userDetails.value.profile_photo_firebase_url.toString())
                        _settingUiState.update {
                            it.copy(
                                isLoading = false,
                            )
                        }

                    }
                }
            }
        }
    }

    private fun editUserDetails(name: String? = null, phone: String? = null) {
        viewModelScope.launch {
            settingsUsesCases.editUserDetails(name, phone).collect { response ->
                when (response) {
                    is Response.Failure -> {
                        _settingUiState.update {
                            it.copy(
                                error = response.e.message,
                                isLoading = false
                            )
                        }
                    }
                    is Response.Success -> {
                        _settingUiState.update {
                            it.copy(
                                isLoading = false,
                            )
                        }
                        //getUserDetails()
                        _eventFlow.emit(UiEvent.ShowMessage(messLabel = R.string.label_user_details_edited_successfully))
                    }
                    is Response.Loading -> {
                        _settingUiState.update {
                            it.copy(
                                isLoading = true
                            )
                        }
                    }
                }
            }
        }
    }

    private fun editUserEmail() {
        viewModelScope.launch {
            settingsUsesCases.editUserEmail(
                email = userDetails.value.email!!,
                password = userDetails.value.password!!
            ).collect { response ->
                when (response) {
                    is Response.Failure -> {
                        _settingUiState.update {
                            it.copy(
                                error = response.e.message,
                                isLoading = false
                            )
                        }
                    }
                    is Response.Success -> {
                        _settingUiState.update {
                            it.copy(
                                isLoading = false,
                            )
                        }
                        _userDetails.update {
                            it.copy(
                                name = null,
                                phone = null,
                                email = null,
                                password = null
                            )
                        }
                        _eventFlow.emit(UiEvent.ShowMessage(R.string.label_successful_registration))
                    }
                    is Response.Loading -> {
                        _settingUiState.update {
                            it.copy(
                                isLoading = true
                            )
                        }
                    }
                }
            }
        }
    }

    fun handleSettingEvent(settingsEvent: SettingsEvent) {
        when (settingsEvent) {
            is SettingsEvent.NameChanged -> {
                _userDetails.update {
                    it.copy(
                        name = settingsEvent.name
                    )
                }
            }
            is SettingsEvent.PhoneChanged -> {
                _userDetails.update {
                    it.copy(
                        phone = settingsEvent.phone
                    )
                }
            }
            is SettingsEvent.EmailChanged -> {
                _userDetails.update {
                    it.copy(
                        email = settingsEvent.email
                    )
                }
            }
            is SettingsEvent.PasswordChanged -> {
                _userDetails.update {
                    it.copy(
                        password = settingsEvent.password
                    )
                }
            }
            is SettingsEvent.ResetUserPassword -> {
                resetUserPassword()
            }
            is SettingsEvent.EditUserDetails -> {
                val name = userDetails.value.name.takeIf { !it.isNullOrEmpty() }
                val phone = userDetails.value.phone.takeIf { !it.isNullOrEmpty() }
                editUserDetails(name = name, phone = phone)

                if (!userDetails.value.email.isNullOrEmpty()) {
                    editUserEmail()
                }
            }
            is SettingsEvent.ErrorDismissed -> {
                _settingUiState.update {
                    it.copy(error = null)
                }
            }
            is SettingsEvent.ChangeProfilePhoto -> {
                showUploadPhotoDialog()
            }
            is SettingsEvent.DismissUploadPhotoDialog -> {
                dismissUploadPhotoDialog()
            }
            is SettingsEvent.UploadOptionChosen -> {
                dismissUploadPhotoDialog()
                if (settingsEvent.photoSource == PhotoSource.STORAGE) {
                    checkReadStoragePermission()
                }
                if (settingsEvent.photoSource == PhotoSource.CAMERA) {

                }
            }
            is SettingsEvent.GetLocalProfilePhotoUri -> {
                Log.d("uri on viewModel", settingsEvent.uri.toString())
                updateProfilePhotoUri(settingsEvent.uri)
            }
            is SettingsEvent.EnableCompression -> {
                enableCompression()
            }
            is SettingsEvent.DisableCompression -> {
                disableCompression()
            }
            is SettingsEvent.GetUserDetails -> {
                getUserDetails()
            }

        }
    }

    private fun updateProfilePhotoUri(uri: Uri) {
        _userDetails.update {
            it.copy(
                profile_photo_uri = uri
            )
        }
    }

    private fun enableCompression() {
        _userDetails.update {
            it.copy(
                enableCompression = true
            )
        }
    }

    private fun disableCompression() {
        _userDetails.update {
            it.copy(
                enableCompression = false
            )
        }
    }

    private fun checkReadStoragePermission() {
        viewModelScope.launch {
            _eventFlow.emit(UiEvent.CheckReadStoragePermission)
        }
    }

    private fun dismissUploadPhotoDialog() {
        _settingUiState.update {
            it.copy(
                showUploadPhotoDialog = false
            )
        }
    }

    private fun showUploadPhotoDialog() {
        _settingUiState.update {
            it.copy(
                showUploadPhotoDialog = true
            )
        }
    }


    private fun resetUserPassword() {
        viewModelScope.launch {
            settingsUsesCases.resetUserPassword().collect { response ->
                when (response) {
                    is Response.Failure -> {
                        _settingUiState.update {
                            it.copy(
                                error = response.e.message,
                                isLoading = false
                            )
                        }
                    }
                    is Response.Success -> {
                        _settingUiState.update {
                            it.copy(
                                isLoading = false,
                                email = "",
                                password = ""
                            )
                        }
                        _eventFlow.emit(UiEvent.ShowMessage(R.string.label_reset_password_link_send))
                    }
                    is Response.Loading -> {
                        _settingUiState.update {
                            it.copy(
                                isLoading = true
                            )
                        }
                    }
                }
            }
        }
    }

}
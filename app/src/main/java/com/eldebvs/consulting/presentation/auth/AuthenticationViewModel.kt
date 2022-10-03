package com.eldebvs.consulting.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eldebvs.consulting.R
import com.eldebvs.consulting.domain.model.Response
import com.eldebvs.consulting.domain.use_case.auth_use_case.AuthUseCases
import com.eldebvs.consulting.presentation.navigation.Screen
import com.eldebvs.consulting.presentation.settings.SettingsEvent
import com.eldebvs.consulting.presentation.settings.UserDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val authUseCases: AuthUseCases
) : ViewModel() {
    private val _uiState = MutableStateFlow(AuthenticationState())
    val uiState = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _userDetails = MutableStateFlow(UserDetails())
    val userDetails = _userDetails.asStateFlow()

    init {
        getAuthState()
        getUserDetails()
    }

    private fun getUserDetails() {
        viewModelScope.launch {
            authUseCases.getUserDetails().collect {response ->
                when(response) {
                    is Response.Failure -> {
                        _uiState.update {
                            it.copy(
                                error = response.e.message,
                                isLoading = false,
                            )
                        }
                    }
                    is Response.Loading -> {
                        _uiState.update {
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
                                email = response.data?.email
                            )
                        }
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                            )
                        }

                    }
                }
            }
        }
    }


    private fun getAuthState() {
        viewModelScope.launch {
            authUseCases.getAuthState.invoke().collect { response ->
                if (response) {
                    _eventFlow.emit(UiEvent.Navigate(Screen.SignedInScreen.route))
                } else {
                    _eventFlow.emit(UiEvent.Navigate(Screen.AuthScreen.route))
                }
            }
        }
    }

    private fun registerUser() {
        viewModelScope.launch(Dispatchers.IO) {
            authUseCases.registerUser(
                email = uiState.value.email!!,
                password = uiState.value.password!!
            ).collect { response ->
                when (response) {
                    is Response.Success -> {
                        _uiState.update {
                            it.copy(
                                email = "",
                                password = "",
                                isLoading = false
                            )
                        }
                        _eventFlow.emit(UiEvent.ShowMessage(messLabel = R.string.label_successful_registration))
                        toggleAuthenticationMode()
                        signOutUser()
                    }
                    is Response.Failure -> {
                        _uiState.update {
                            it.copy(
                                error = response.e.message,
                                isLoading = false
                            )
                        }
                    }
                    is Response.Loading -> {
                        _uiState.update {
                            it.copy(
                                isLoading = true
                            )
                        }
                    }

                }

            }
        }
    }

    private fun signOutUser() {
        viewModelScope.launch(Dispatchers.IO) {
            authUseCases.signOutUser().collect { response ->
                when (response) {
                    is Response.Failure -> {
                        _uiState.update {
                            it.copy(
                                error = response.e.message,
                                isLoading = false
                            )
                        }
                    }
                    is Response.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                            )
                        }

                    }
                    is Response.Loading -> {
                        _uiState.update {
                            it.copy(
                                isLoading = true
                            )
                        }
                    }
                }
            }
        }
    }

    private fun signInUser(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            authUseCases.signInUser(email, password).collect { response ->
                when (response) {
                    is Response.Failure -> {
                        _uiState.update {
                            it.copy(
                                error = response.e.message,
                                isLoading = false
                            )
                        }
                    }
                    is Response.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                email = "",
                                password = ""
                            )
                        }
                    }
                    is Response.Loading -> {
                        _uiState.update {
                            it.copy(
                                isLoading = true
                            )
                        }
                    }
                }

            }

        }
    }

    private fun toggleAuthenticationMode() {
        val authenticationMode = uiState.value.authenticationMode

        val newAuthenticationMode = if (authenticationMode == AuthenticationMode.SIGN_IN) {
            AuthenticationMode.SIGN_UP
        } else AuthenticationMode.SIGN_IN

        _uiState.update {
            it.copy(
                authenticationMode = newAuthenticationMode
            )
        }
    }

    private fun emailChange(email: String) {
        _uiState.update {
            it.copy(
                email = email
            )
        }
    }

    private fun passwordChange(password: String) {
        val requirements = mutableListOf<PasswordRequirement>()
        if (password.length > 8) {
            requirements.add(PasswordRequirement.EIGHT_CHARACTERS)
        }
        if (password.any() { it.isDigit() }) {
            requirements.add(PasswordRequirement.NUMBER)
        }
        if (password.any() { it.isUpperCase() }) {
            requirements.add(PasswordRequirement.CAPITAL_LETTER)
        }
        _uiState.update {
            it.copy(
                password = password,
                passwordRequirements = requirements
            )
        }
    }

    private fun dismissError() {
        _uiState.update {
            it.copy(
                error = null,
                email = null,
                password = null,
                isLoading = false
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
    }

    private fun resendVerificationEmail(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            authUseCases.resendVerificationEmail(email, password).collect { response ->
                when (response) {
                    is Response.Loading -> {
                        _uiState.update {
                            it.copy(
                                isLoading = true
                            )
                        }
                    }
                    is Response.Failure -> {
                        _uiState.update {
                            it.copy(
                                error = response.e.message,
                                isLoading = false
                            )
                        }
                    }
                    is Response.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                email = "",
                                password = ""
                            )
                        }
                        toggleResendEmailDialogVisibility()
                        _eventFlow.emit(UiEvent.ShowMessage(R.string.label_successful_registration))
                    }
                }

            }
        }
    }

    fun handleEvent(authenticationEvent: AuthenticationEvent) {
        when (authenticationEvent) {
            is AuthenticationEvent.ToggleAuthenticationMode -> {
                toggleAuthenticationMode()
            }
            is AuthenticationEvent.EmailChanged -> {
                emailChange(authenticationEvent.emailAddress)
            }
            is AuthenticationEvent.PasswordChanged -> {
                passwordChange(authenticationEvent.password)
            }
            is AuthenticationEvent.Authenticate -> {
                if (uiState.value.authenticationMode == AuthenticationMode.SIGN_UP) {
                    registerUser()
                } else {
                    signInUser(uiState.value.email!!, uiState.value.password!!)
                }
            }
            is AuthenticationEvent.ErrorDismissed -> {
                dismissError()
            }
            is AuthenticationEvent.ToggleResendEmailDialogVisibility -> {
                toggleResendEmailDialogVisibility()
            }
            is AuthenticationEvent.ResendVerificationEmail -> {
                resendVerificationEmail(uiState.value.email!!, uiState.value.password!!)
            }
            is AuthenticationEvent.SignOutUser -> {
                signOutUser()
            }
            is AuthenticationEvent.RefreshAuthState -> {

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

        }

    }

    private fun editUserDetails(name: String? = null, phone: String? = null) {
        viewModelScope.launch {
            authUseCases.editUserDetails(name, phone).collect { response ->
                when (response) {
                    is Response.Failure -> {
                        _uiState.update {
                            it.copy(
                                error = response.e.message,
                                isLoading = false
                            )
                        }
                    }
                    is Response.Success -> {
                        _uiState.update {
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
                        _eventFlow.emit(UiEvent.ShowMessage(messLabel = R.string.label_user_details_edited_successfully))
                    }
                    is Response.Loading -> {
                        _uiState.update {
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
            authUseCases.editUserEmail(
                email = userDetails.value.email!!,
                password = userDetails.value.password!!
            ).collect { response ->
                when (response) {
                    is Response.Failure -> {
                        _uiState.update {
                            it.copy(
                                error = response.e.message,
                                isLoading = false
                            )
                        }
                    }
                    is Response.Success -> {
                        _uiState.update {
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
                        _uiState.update {
                            it.copy(
                                isLoading = true
                            )
                        }
                    }
                }
            }
        }
    }

    private fun resetUserPassword() {
        viewModelScope.launch {
            authUseCases.resetUserPassword().collect { response ->
                when (response) {
                    is Response.Failure -> {
                        _uiState.update {
                            it.copy(
                                error = response.e.message,
                                isLoading = false
                            )
                        }
                    }
                    is Response.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                email = "",
                                password = ""
                            )
                        }
                        _eventFlow.emit(UiEvent.ShowMessage(R.string.label_reset_password_link_send))
                    }
                    is Response.Loading -> {
                        _uiState.update {
                            it.copy(
                                isLoading = true
                            )
                        }
                    }
                }
            }
        }
    }

    private fun toggleResendEmailDialogVisibility() {
        _uiState.update {
            it.copy(
                showResendEmailDialog = !uiState.value.showResendEmailDialog,
                email = "",
                password = ""
            )
        }
    }

    sealed class UiEvent() {
        data class ShowMessage(val messLabel: Int) : UiEvent()
        data class Navigate(val route: String) : UiEvent()
    }
}
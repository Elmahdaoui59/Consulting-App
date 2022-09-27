package com.eldebvs.consulting.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eldebvs.consulting.R
import com.eldebvs.consulting.domain.model.Response
import com.eldebvs.consulting.domain.use_case.auth_use_case.AuthUseCases
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

    init {
      getAuthState()
    }
    private fun getAuthState() {
        viewModelScope.launch {
            authUseCases.getAuthState.invoke().collect{ state ->
                _uiState.update {
                    it.copy(
                        isUserAuthenticated = state
                    )
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
                signOutUser()
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
                                isUserAuthenticated = false
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
                when(response) {
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
                        getAuthState()
                        if (uiState.value.isUserAuthenticated) {
                            _eventFlow.emit(UiEvent.ShowMessage(messLabel = R.string.label_successful_sign_In))
                        } else {
                            _eventFlow.emit(UiEvent.ShowMessage(messLabel = R.string.label_email_verification_request))
                            signOutUser()
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
                error = null
            )
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
            is AuthenticationEvent.ToggleResetEmailDialogVisiblity -> {
                _uiState.update {
                    it.copy(
                        showResetEmailDialog = !uiState.value.showResetEmailDialog
                    )
                }
            }
        }
    }

    sealed class UiEvent(){
        data class ShowMessage(val messLabel: Int): UiEvent()
    }
}
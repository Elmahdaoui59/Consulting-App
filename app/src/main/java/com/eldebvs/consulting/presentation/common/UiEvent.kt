package com.eldebvs.consulting.presentation.common


sealed class UiEvent {
    data class ShowMessage(val messLabel: Int) : UiEvent()
    data class Navigate(val route: String) : UiEvent()
    object CheckReadStoragePermission: UiEvent()
    object CheckCameraPermission: UiEvent()
}
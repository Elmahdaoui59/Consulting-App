package com.eldebvs.consulting.presentation.navigation

sealed class Screen(val route: String) {
    object AuthScreen: Screen(route = "auth_screen")
    object SignedInScreen: Screen(route = "signed_in_screen")
}

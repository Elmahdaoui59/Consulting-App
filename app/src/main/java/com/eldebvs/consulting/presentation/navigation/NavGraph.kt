package com.eldebvs.consulting.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.eldebvs.consulting.presentation.auth.*
import com.eldebvs.consulting.presentation.settings.SettingAccountScreen
import com.eldebvs.consulting.presentation.user.SignedInScreen

@Composable
fun SetupNavGraph(
    navController: NavHostController,
    authViewModel: AuthenticationViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.AuthScreen.route
    ) {
        composable(
            route = Screen.AuthScreen.route
        ) {
            AuthenticationScreen(authViewModel)
        }

        composable(
            route = Screen.SignedInScreen.route
        ) {
            SignedInScreen(navController, authViewModel)
        }
        composable(
            route = Screen.SettingAccountScreen.route
        ) {
            SettingAccountScreen(authViewModel = authViewModel)
        }
    }

}
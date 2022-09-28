package com.eldebvs.consulting.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import com.eldebvs.consulting.presentation.auth.AuthenticationScreen
import com.eldebvs.consulting.presentation.auth.AuthenticationViewModel
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
            SignedInScreen(authViewModel)
        }
    }

}
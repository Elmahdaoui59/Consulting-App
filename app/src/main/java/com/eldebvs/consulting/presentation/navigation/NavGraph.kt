package com.eldebvs.consulting.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.eldebvs.consulting.presentation.auth.*
import com.eldebvs.consulting.presentation.chat.ChatScreen
import com.eldebvs.consulting.presentation.chat.components.ChatSession
import com.eldebvs.consulting.presentation.settings.SettingAccountScreen
import com.eldebvs.consulting.presentation.settings.SettingsViewModel
import com.eldebvs.consulting.presentation.user.Dashboard

@Composable
fun SetupNavGraph(
    navController: NavHostController,
    authViewModel: AuthenticationViewModel,
    settingsViewModel: SettingsViewModel
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
            Dashboard(navController, authViewModel)
        }
        composable(
            route = Screen.SettingAccountScreen.route
        ) {
            SettingAccountScreen(settingsViewModel)
        }
        composable(
            route = Screen.ChatScreen.route
        ) {
            ChatScreen(
                chatViewModel = hiltViewModel(),
                navController = navController
            )
        }
        composable(
            route = Screen.ChatSession.route
        ) {
            ChatSession()
        }
    }

}
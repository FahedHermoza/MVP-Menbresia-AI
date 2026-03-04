package com.fahed.perupass.feature.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fahed.perupass.feature.screen.auth.LoginScreen
import com.fahed.perupass.feature.screen.placeholder.FeedPlaceholderScreen

@Composable
fun AppNavHost(
    startDestination: String = NavRoutes.LOGIN,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(NavRoutes.LOGIN) {
            LoginScreen(
                onNavigateToFeed = {
                    navController.navigate(NavRoutes.FEED) {
                        popUpTo(NavRoutes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        composable(NavRoutes.FEED) {
            FeedPlaceholderScreen()
        }
    }
}

object NavRoutes {
    const val LOGIN = "login"
    const val FEED = "feed"
}
